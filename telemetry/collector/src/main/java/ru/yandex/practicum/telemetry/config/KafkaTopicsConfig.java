package ru.yandex.practicum.telemetry.config;

import lombok.Getter;

@Getter
public enum KafkaTopicsConfig {
    SENSOR_EVENT_TOPIC("telemetry.sensors.v1"),
    HUB_EVENT_TOPIC("telemetry.hubs.v1");

    private final String topic;

    KafkaTopicsConfig(String topic) {
        this.topic = topic;
    }
}
