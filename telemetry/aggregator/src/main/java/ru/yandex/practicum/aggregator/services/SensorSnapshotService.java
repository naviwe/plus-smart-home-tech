package ru.yandex.practicum.aggregator.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SensorSnapshotService {

    private final Map<String, SensorsSnapshotAvro> snapshots = new ConcurrentHashMap<>();

    public Optional<SensorsSnapshotAvro> updateSnapshot(SensorEventAvro event) {
        if (event == null || event.getPayload() == null) {
            return Optional.empty();
        }

        String hubId = event.getHubId();
        String sensorId = event.getId();

        log.debug("Updating snapshot: hubId={}, sensorId={}, timestamp={}", hubId, sensorId, event.getTimestamp());

        SensorsSnapshotAvro snapshot = snapshots.computeIfAbsent(hubId, id -> {
            log.info("Created new snapshot for hub: {}", hubId);
            SensorsSnapshotAvro snap = new SensorsSnapshotAvro();
            snap.setHubId(hubId);
            snap.setTimestamp(event.getTimestamp());
            snap.setSensorsState(new HashMap<>());
            return snap;
        });

        Map<String, SensorStateAvro> sensorsState = snapshot.getSensorsState();
        SensorStateAvro oldState = sensorsState.get(sensorId);

        if (oldState != null) {
            long oldTs = oldState.getTimestamp().toEpochMilli();
            long newTs = event.getTimestamp().toEpochMilli();

            if (oldTs > newTs) {
                log.debug("Skipped outdated event from sensor {}: old ts={}, new ts={}", sensorId, oldTs, newTs);
                return Optional.empty();
            }

            Object newPayload = event.getPayload();
            Object oldPayload = oldState.getData();

            if (!oldPayload.getClass().equals(newPayload.getClass())) {
                log.warn("Payload types mismatch: old={}, new={}", oldPayload.getClass(), newPayload.getClass());
            } else if (!isDataChanged(oldPayload, newPayload)) {
                log.debug("{}: data unchanged", newPayload.getClass().getSimpleName());
                return Optional.empty();
            }
            log.debug("Updating sensor state {}: old ts={}, new ts={}", sensorId, oldTs, newTs);
        } else {
            log.debug("Adding new sensor to snapshot: {}", sensorId);
        }

        SensorStateAvro newState = new SensorStateAvro();
        newState.setTimestamp(event.getTimestamp());
        newState.setData(event.getPayload());
        sensorsState.put(sensorId, newState);
        snapshot.setTimestamp(event.getTimestamp());
        log.info("Sensor state {} updated. Updated snapshot for hub {}: timestamp={}", sensorId, hubId, snapshot.getTimestamp());
        return Optional.of(snapshot);
    }

    private boolean isDataChanged(Object oldPayload, Object newPayload) {
        if (oldPayload instanceof ClimateSensorAvro oldClimate && newPayload instanceof ClimateSensorAvro newClimate) {
            return oldClimate.getTemperatureC() != newClimate.getTemperatureC() ||
                    oldClimate.getHumidity() != newClimate.getHumidity() ||
                    oldClimate.getCo2Level() != newClimate.getCo2Level();
        } else if (oldPayload instanceof LightSensorAvro oldLight && newPayload instanceof LightSensorAvro newLight) {
            return oldLight.getLinkQuality() != newLight.getLinkQuality() ||
                    oldLight.getLuminosity() != newLight.getLuminosity();
        } else if (oldPayload instanceof MotionSensorAvro oldMotion && newPayload instanceof MotionSensorAvro newMotion) {
            return oldMotion.getLinkQuality() != newMotion.getLinkQuality() ||
                    oldMotion.getMotion() != newMotion.getMotion() ||
                    oldMotion.getVoltage() != newMotion.getVoltage();
        } else if (oldPayload instanceof SwitchSensorAvro oldSwitch && newPayload instanceof SwitchSensorAvro newSwitch) {
            return oldSwitch.getState() != newSwitch.getState();
        } else if (oldPayload instanceof TemperatureSensorAvro oldTemp && newPayload instanceof TemperatureSensorAvro newTemp) {
            return oldTemp.getTemperatureC() != newTemp.getTemperatureC() ||
                    oldTemp.getTemperatureF() != newTemp.getTemperatureF();
        }
        return true;
    }
}