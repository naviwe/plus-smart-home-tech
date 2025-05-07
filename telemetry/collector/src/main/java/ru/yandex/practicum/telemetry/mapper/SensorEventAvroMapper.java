package ru.yandex.practicum.telemetry.mapper;


import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.model.*;
import ru.yandex.practicum.telemetry.model.SensorEvent;

public class SensorEventAvroMapper {

    public static SensorEventAvro mapSensorEvent(SensorEvent event) {
        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(mapPayload(event))
                .build();
    }

    private static Object mapPayload(SensorEvent event) {
        return switch (event.getType()) {
            case TEMPERATURE_SENSOR_EVENT -> mapPayload((TemperatureSensorEvent) event);
            case LIGHT_SENSOR_EVENT -> mapPayload((LightSensorEvent) event);
            case CLIMATE_SENSOR_EVENT -> mapPayload((ClimateSensorEvent) event);
            case SWITCH_SENSOR_EVENT -> mapPayload((SwitchSensorEvent) event);
            case MOTION_SENSOR_EVENT -> mapPayload((MotionSensorEvent) event);
        };
    }

    private static LightSensorAvro mapPayload(LightSensorEvent event) {
        return LightSensorAvro.newBuilder()
                .setLinkQuality(event.getLinkQuality())
                .setLuminosity(event.getLuminosity())
                .build();
    }

    private static MotionSensorAvro mapPayload(MotionSensorEvent event) {
        return MotionSensorAvro.newBuilder()
                .setLinkQuality(event.getLinkQuality())
                .setMotion(event.getMotion())
                .setVoltage(event.getVoltage())
                .build();
    }

    private static SwitchSensorAvro mapPayload(SwitchSensorEvent event) {
        return SwitchSensorAvro.newBuilder()
                .setState(event.getState())
                .build();
    }

    private static ClimateSensorAvro mapPayload(ClimateSensorEvent event) {
        return ClimateSensorAvro.newBuilder()
                .setCo2Level(event.getCo2Level())
                .setHumidity(event.getHumidity())
                .setTemperatureC(event.getTemperatureC())
                .build();
    }

    private static TemperatureSensorAvro mapPayload(TemperatureSensorEvent event) {
        return TemperatureSensorAvro.newBuilder()
                .setTemperatureC(event.getTemperatureC())
                .setTemperatureF(event.getTemperatureF())
                .build();
    }
}