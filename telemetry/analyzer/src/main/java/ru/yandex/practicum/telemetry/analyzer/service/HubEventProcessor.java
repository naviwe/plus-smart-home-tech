package ru.yandex.practicum.telemetry.analyzer.service;

import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.analyzer.kafka.KafkaHubEventConsumer;
import ru.yandex.practicum.telemetry.analyzer.model.Scenario;
import ru.yandex.practicum.telemetry.analyzer.model.ScenarioAction;
import ru.yandex.practicum.telemetry.analyzer.model.ScenarioCondition;
import ru.yandex.practicum.telemetry.analyzer.model.Sensor;
import ru.yandex.practicum.telemetry.analyzer.repository.*;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;
import ru.yandex.practicum.telemetry.analyzer.model.Action;

import java.time.Duration;
import java.util.List;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HubEventProcessor implements Runnable {

    KafkaHubEventConsumer consumer;
    SensorRepository sensorRepository;
    ScenarioRepository scenarioRepository;
    ConditionRepository conditionRepository;
    ActionRepository actionRepository;
    ScenarioConditionRepository scenarioConditionRepository;
    ScenarioActionRepository scenarioActionRepository;
    String hubsTopic;

    public HubEventProcessor(
            KafkaHubEventConsumer consumer,
            SensorRepository sensorRepository,
            ScenarioRepository scenarioRepository,
            ConditionRepository conditionRepository,
            ActionRepository actionRepository,
            ScenarioConditionRepository scenarioConditionRepository,
            ScenarioActionRepository scenarioActionRepository,
            @Value("${kafka.topics.hubs}") String hubsTopic) {
        this.consumer = consumer;
        this.sensorRepository = sensorRepository;
        this.scenarioRepository = scenarioRepository;
        this.conditionRepository = conditionRepository;
        this.actionRepository = actionRepository;
        this.scenarioConditionRepository = scenarioConditionRepository;
        this.scenarioActionRepository = scenarioActionRepository;
        this.hubsTopic = hubsTopic;

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutdown hook triggered, waking up hub event consumer...");
            consumer.wakeup();
        }));
    }

    @PostConstruct
    public void init() {
        log.info("Initializing subscription to topic telemetry.hubs.v1");
        consumer.subscribe(List.of("telemetry.hubs.v1"));
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                var records = consumer.poll(Duration.ofMillis(100));
                for (var record : records) {
                    var hubEvent = record.value();
                    log.debug("Processing snapshot: {}", hubEvent);
                    try {
                        process(hubEvent);
                    } catch (Exception e) {
                        log.error("Error processing HubEvent: {}", e.getMessage(), e);
                    }
                }
                try {
                    consumer.commit();
                } catch (Exception e) {
                    log.error("Error committing offsets", e);
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

    public void process(HubEventAvro event) {
        var payload = event.getPayload();

        switch (payload.getClass().getSimpleName()) {
            case "DeviceAddedEventAvro" -> handleDeviceAdded(event);
            case "DeviceRemovedEventAvro" -> handleDeviceRemoved(event);
            case "ScenarioAddedEventAvro" -> handleScenarioAdded(event);
            case "ScenarioRemovedEventAvro" -> handleScenarioRemoved(event);
            default -> log.warn("Unhandled event type: {}", payload.getClass().getSimpleName());
        }
    }

    private void handleDeviceAdded(HubEventAvro event) {
        var data = (DeviceAddedEventAvro) event.getPayload();
        sensorRepository.save(new Sensor(data.getId(), event.getHubId()));
        log.info("Sensor added: {}", data.getId());
    }

    private void handleDeviceRemoved(HubEventAvro event) {
        var data = (DeviceRemovedEventAvro) event.getPayload();
        sensorRepository.deleteById(data.getId());
        log.info("Sensor removed: {}", data.getId());
    }

    private void handleScenarioAdded(HubEventAvro event) {
        var data = (ScenarioAddedEventAvro) event.getPayload();
        var scenarioName = data.getName();
        var hubId = event.getHubId();

        if (scenarioRepository.findByHubIdAndName(hubId, scenarioName).isPresent()) {
            log.warn("Scenario already exists: {} for hub {}", scenarioName, hubId);
            return;
        }

        var scenario = scenarioRepository.save(new Scenario(null, hubId, scenarioName));
        log.info("Scenario added: {} (ID: {})", scenarioName, scenario.getId());

        for (var cond : data.getConditions()) {
            Object avroValue = cond.getValue();
            final Integer conditionValue;

            if (avroValue instanceof Boolean boolValue) {
                conditionValue = boolValue ? 1 : 0;
            } else if (avroValue instanceof Integer intValue) {
                conditionValue = intValue;
            } else if (avroValue != null) {
                log.warn("Unexpected condition value type: {} ({})", avroValue, avroValue.getClass().getName());
                conditionValue = null;
            } else {
                conditionValue = null;
            }

            sensorRepository.findById(cond.getSensorId()).ifPresentOrElse(sensor -> {
                var savedCondition = conditionRepository.save(
                        new Condition(null, cond.getType().name(), cond.getOperation().name(), conditionValue)
                );
                scenarioConditionRepository.save(new ScenarioCondition(scenario, sensor, savedCondition));
                log.debug("Condition added: sensorId={}, type={}, op={}, value={}",
                        cond.getSensorId(), cond.getType(), cond.getOperation(), conditionValue);
            }, () -> log.error("Sensor not found for condition: sensorId={}", cond.getSensorId()));
        }

        for (var act : data.getActions()) {
            Integer actionValue = act.getValue();

            sensorRepository.findById(act.getSensorId()).ifPresentOrElse(sensor -> {
                var savedAction = actionRepository.save(
                        new Action(null, act.getType().name(), actionValue)
                );
                scenarioActionRepository.save(new ScenarioAction(scenario, sensor, savedAction));
                log.debug("Action added: sensorId={}, type={}, value={}",
                        act.getSensorId(), act.getType(), actionValue);
            }, () -> log.error("Sensor not found for action: sensorId={}", act.getSensorId()));
        }

        log.info("Completed adding scenario '{}'", data.getName());
    }

    private void handleScenarioRemoved(HubEventAvro event) {
        var data = (ScenarioRemovedEventAvro) event.getPayload();
        var scenario = scenarioRepository.findByHubIdAndName(event.getHubId(), data.getName())
                .orElseThrow();
        scenarioRepository.delete(scenario);
        log.info("Scenario removed: {}", data.getName());
    }
}