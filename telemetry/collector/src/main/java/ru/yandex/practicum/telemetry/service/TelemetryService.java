package ru.yandex.practicum.telemetry.service;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.config.KafkaConfig;
import ru.yandex.practicum.telemetry.mapper.HubEventAvroMapper;
import ru.yandex.practicum.telemetry.mapper.SensorEventAvroMapper;
import ru.yandex.practicum.telemetry.model.HubEvent;
import ru.yandex.practicum.telemetry.model.SensorEvent;

import java.util.Properties;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelemetryService {

    private final KafkaProducer<String, SpecificRecordBase> producer;
    private final KafkaConfig kafkaConfig;

    public TelemetryService(KafkaConfig kafkaConfig) {
        this.kafkaConfig = kafkaConfig;

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaConfig.getProducer().getKeySerializer());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaConfig.getProducer().getValueSerializer());

        this.producer = new KafkaProducer<>(props);
    }

    public void processSensorEvent(SensorEvent event) {
        try {
            SensorEventAvro sensorEventAvro = SensorEventAvroMapper.mapSensorEvent(event);
            String topic = kafkaConfig.getTopics().getSensor();
            ProducerRecord<String, SpecificRecordBase> record =
                    new ProducerRecord<>(topic, sensorEventAvro.getHubId(), sensorEventAvro);
            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    log.error("Error sending sensor event to topic [{}]: {}", topic, exception.getMessage(), exception);
                } else {
                    log.info("Sensor event sent to topic [{}] with offset {}", topic, metadata.offset());
                }
            });
        } catch (Exception ex) {
            log.error("Error processing sensor event: {}", ex.getMessage(), ex);
        }
    }

    public void processHubEvent(HubEvent event) {
        try {
            HubEventAvro hubEventAvro = HubEventAvroMapper.mapSensorEvent(event);
            String topic = kafkaConfig.getTopics().getHub();
            ProducerRecord<String, SpecificRecordBase> record =
                    new ProducerRecord<>(topic, event.getType().name(), hubEventAvro);
            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    log.error("Error sending hub event to topic [{}]: {}", topic, exception.getMessage(), exception);
                } else {
                    log.info("Hub event sent to topic [{}] with offset {}", topic, metadata.offset());
                }
            });
        } catch (Exception ex) {
            log.error("Error processing hub event: {}", ex.getMessage(), ex);
        }
    }

    @PreDestroy
    public void close() {
        producer.close();
    }
}