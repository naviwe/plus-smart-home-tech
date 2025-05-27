package ru.yandex.practicum.telemetry.analyzer.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.analyzer.client.HubRouterGrpcClient;
import ru.yandex.practicum.telemetry.analyzer.kafka.KafkaSnapshotConsumer;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;
import ru.yandex.practicum.telemetry.analyzer.model.Scenario;
import ru.yandex.practicum.telemetry.analyzer.model.ScenarioAction;
import ru.yandex.practicum.telemetry.analyzer.model.ScenarioCondition;
import ru.yandex.practicum.telemetry.analyzer.repository.ScenarioActionRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.ScenarioConditionRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.ScenarioRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SnapshotProcessor {

    ScenarioRepository scenarioRepository;
    ScenarioConditionRepository scenarioConditionRepository;
    ScenarioActionRepository scenarioActionRepository;
    HubRouterGrpcClient client;
    KafkaSnapshotConsumer consumer;
    String snapshotsTopic;

    public SnapshotProcessor(
            ScenarioRepository scenarioRepository,
            ScenarioConditionRepository scenarioConditionRepository,
            ScenarioActionRepository scenarioActionRepository,
            HubRouterGrpcClient client,
            KafkaSnapshotConsumer consumer,
            @Value("${kafka.topics.snapshots}") String snapshotsTopic) {
        this.scenarioRepository = scenarioRepository;
        this.scenarioConditionRepository = scenarioConditionRepository;
        this.scenarioActionRepository = scenarioActionRepository;
        this.client = client;
        this.consumer = consumer;
        this.snapshotsTopic = snapshotsTopic;

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutdown hook triggered, waking up snapshot consumer...");
            consumer.wakeup();
        }));
    }

    public void start() {
        log.info("Initializing subscription to topic telemetry.snapshots.v1");
        consumer.subscribe(List.of("telemetry.snapshots.v1"));
        try {
            while (true) {
                Thread.sleep(2000);
                var records = consumer.poll(Duration.ofMillis(100));
                if (!records.isEmpty()) {
                    for (var record : records) {
                        var snapshot = record.value();
                        log.debug("Processing snapshot: {}", snapshot);
                        try {
                            process(snapshot);
                        } catch (Exception e) {
                            log.error("Error processing snapshot: {}", snapshot, e);
                        }
                    }
                    try {
                        consumer.commit();
                    } catch (Exception e) {
                        log.error("Error committing offsets", e);
                    }
                }
            }
        } catch (WakeupException e) {
            log.info("Received shutdown signal (WakeupException)");
        } catch (Exception e) {
            log.error("Critical error during event processing", e);
        } finally {
            try {
                log.info("Closing consumer");
                consumer.close();
                log.info("Consumer closed successfully");
            } catch (Exception e) {
                log.error("Error closing consumer", e);
            }
        }
    }

    public void process(SensorsSnapshotAvro snapshot) {
        log.warn("process() method triggered for snapshot: {}", snapshot);
        String hubId = snapshot.getHubId();
        Instant ts = snapshot.getTimestamp();

        log.info("Processing snapshot for hub {} at {}", hubId, ts);
        log.debug("Sensors in snapshot: {}", snapshot.getSensorsState().keySet());
        snapshot.getSensorsState()
                .forEach((k, v) -> log.debug("  [{}] → {}", k, v.getData().getClass()));

        var scenarios = scenarioRepository.findAllByHubId(hubId);
        log.debug("Found {} scenarios for hub {}", scenarios.size(), hubId);

        for (Scenario scenario : scenarios) {
            log.debug("Checking scenario: {}", scenario.getName());
            var conditions = scenarioConditionRepository.findAllByScenarioId(scenario.getId());

            log.debug("Scenario conditions ({}): {}", conditions.size(), conditions);
            if (isSatisfied(conditions, snapshot)) {
                log.info("Scenario '{}' activated", scenario.getName());
                var actions = scenarioActionRepository.findAllByScenarioId(scenario.getId());

                for (ScenarioAction action : actions) {
                    String sensorId = action.getSensor().getId();
                    String type = action.getAction().getType();
                    Integer value = action.getAction().getValue();
                    log.info("Sending command: sensorId={}, type={}, value={}", sensorId, type, value);
                    log.warn("Conditions met, sending command: {}", action);
                    client.sendDeviceAction(hubId, scenario.getName(), sensorId, value, type);
                }
            } else {
                log.warn("Conditions not met: {}", scenario.getName());
            }
        }
    }

    private boolean isSatisfied(List<ScenarioCondition> conditions, SensorsSnapshotAvro snapshot) {
        for (ScenarioCondition condition : conditions) {
            var sensorId = condition.getSensor().getId();
            var state = snapshot.getSensorsState().get(sensorId);

            if (state == null) {
                log.warn("Sensor data missing for '{}' in snapshot", sensorId);
                log.warn("Sensors in snapshot: {}", snapshot.getSensorsState().keySet());
                return false;
            }

            log.debug("Found sensor '{}': {}", sensorId, state);
            boolean conditionMet = evaluateCondition(condition.getCondition(), state.getData());
            log.debug("Condition for sensor {}: {}", sensorId, conditionMet ? "met" : "not met");

            if (!conditionMet) return false;
        }
        return true;
    }

    private boolean evaluateCondition(Condition condition, Object data) {
        String type = condition.getType();

        if (data == null) {
            log.warn("Sensor data is null (data == null) for condition: {}", condition);
            return false;
        }

        log.debug("Snapshot data type: {}", data.getClass().getName());
        String operation = condition.getOperation();
        Integer expectedValue = condition.getValue();

        if (expectedValue == null) {
            log.error("Expected value in condition is null: {}", condition);
            return false;
        }

        log.debug("Checking condition: type={}, operation={}, expected={}", type, operation, expectedValue);
        switch (type) {
            case "LUMINOSITY" -> {
                log.debug("Checking LUMINOSITY type for data={}", data.getClass().getName());
                if (data instanceof LightSensorAvro light) {
                    return compareAndLog("LUMINOSITY", light.getLuminosity(), expectedValue, operation);
                } else {
                    log.warn("Expected LightSensorAvro but got {}", data.getClass().getName());
                    return false;
                }
            }
            case "TEMPERATURE" -> {
                log.debug("Checking TEMPERATURE type for data={}", data.getClass().getName());
                if (data instanceof ClimateSensorAvro climate) {
                    return compareAndLog("TEMPERATURE (climate)", climate.getTemperatureC(), expectedValue,
                            operation);
                } else if (data instanceof TemperatureSensorAvro temp) {
                    boolean resultC = compareAndLog("TEMPERATURE (temp, °C)", temp.getTemperatureC(),
                            expectedValue, operation);
                    boolean resultF = compareAndLog("TEMPERATURE (temp, °F)", temp.getTemperatureF(),
                            expectedValue, operation);
                    return resultC || resultF;
                } else {
                    log.warn("Expected ClimateSensorAvro or TemperatureSensorAvro but got {}",
                            data.getClass().getName());
                    return false;
                }
            }
            case "CO2LEVEL" -> {
                log.debug("Checking CO2LEVEL type for data={}", data.getClass().getName());
                if (data instanceof ClimateSensorAvro climate) {
                    return compareAndLog("CO2LEVEL", climate.getCo2Level(), expectedValue, operation);
                } else {
                    log.warn("Expected ClimateSensorAvro but got {}", data.getClass().getName());
                    return false;
                }
            }
            case "HUMIDITY" -> {
                log.debug("Checking HUMIDITY type for data={}", data.getClass().getName());
                if (data instanceof ClimateSensorAvro climate) {
                    return compareAndLog("HUMIDITY", climate.getHumidity(), expectedValue, operation);
                } else {
                    log.warn("Expected ClimateSensorAvro but got {}", data.getClass().getName());
                    return false;
                }
            }
            case "MOTION" -> {
                log.debug("Checking MOTION type for data={}", data.getClass().getName());
                if (data instanceof MotionSensorAvro motion) {
                    return compareAndLog("MOTION", motion.getMotion() ? 1 : 0, expectedValue, operation);
                } else {
                    log.warn("Expected MotionSensorAvro but got {}", data.getClass().getName());
                    return false;
                }
            }
            case "SWITCH" -> {
                log.debug("Checking SWITCH type for data={}", data.getClass().getName());
                if (data instanceof SwitchSensorAvro sw) {
                    return compareAndLog("SWITCH", sw.getState() ? 1 : 0, expectedValue, operation);
                } else {
                    log.warn("Expected SwitchSensorAvro but got {}", data.getClass().getName());
                    return false;
                }
            }
            default -> {
                log.warn("Unknown condition type: {}", type);
                return false;
            }
        }
    }

    private boolean compare(int actual, int expected, String operation) {
        return switch (operation) {
            case "EQUALS" -> actual == expected;
            case "GREATER_THAN" -> actual > expected;
            case "LOWER_THAN" -> actual < expected;
            default -> false;
        };
    }

    private boolean compareAndLog(String label, int actual, int expected, String operation) {
        boolean result = compare(actual, expected, operation);
        log.debug("{}: {} {} {} → {}", label, actual, operation, expected, result ? "met" : "not met");
        return result;
    }
}