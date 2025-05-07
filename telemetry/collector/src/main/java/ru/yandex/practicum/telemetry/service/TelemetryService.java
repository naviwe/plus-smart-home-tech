package ru.yandex.practicum.telemetry.service;

import com.example.avro.HubEventAvro;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.mapper.HubEventAvroMapper;
import ru.yandex.practicum.telemetry.mapper.SensorEventAvroMapper;
import ru.yandex.practicum.telemetry.model.HubEvent;
import ru.yandex.practicum.telemetry.model.SensorEvent;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static ru.yandex.practicum.telemetry.config.KafkaTopicsConfig.HUB_EVENT_TOPIC;
import static ru.yandex.practicum.telemetry.config.KafkaTopicsConfig.SENSOR_EVENT_TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelemetryService {
    private final KafkaTemplate<String, HubEventAvro> kafkaHubTemplate;
    private final KafkaTemplate<String, SensorEventAvro> kafkaSensorTemplate;

    public void processSensorEvent(SensorEvent event) {
        SensorEventAvro sensorEventAvro = SensorEventAvroMapper.mapSensorEvent(event);
        CompletableFuture<SendResult<String, SensorEventAvro>> future =
                kafkaSensorTemplate.send(SENSOR_EVENT_TOPIC.getTopic(), sensorEventAvro.getHubId(), sensorEventAvro);

        future.whenComplete((result, ex) -> {
            if (Objects.nonNull(ex)) {
                log.error(ex.getMessage(), ex);
            }
        });
    }

    public void processHubEvent(HubEvent event) {
        HubEventAvro hubEventAvro = HubEventAvroMapper.mapSensorEvent(event);
        CompletableFuture<SendResult<String, HubEventAvro>> future =
                kafkaHubTemplate.send(HUB_EVENT_TOPIC.getTopic(), event.getType().name(), hubEventAvro);

        future.whenComplete((result, ex) -> {
            if (Objects.nonNull(ex)) {
                log.error(ex.getMessage(), ex);
            }
        });
    }
}