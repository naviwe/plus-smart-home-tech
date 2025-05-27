package ru.yandex.practicum.telemetry.analyzer.kafka;

import jakarta.annotation.PreDestroy;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.practicum.telemetry.deserialization.SensorsSnapshotDeserializer;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Component
public class KafkaSnapshotConsumer {

    private final KafkaConsumer<String, SensorsSnapshotAvro> consumer;

    public KafkaSnapshotConsumer(
            @Value("${spring.kafka.snapshot-consumer.bootstrap-servers}") String bootstrapServers,
            @Value("${spring.kafka.snapshot-consumer.group-id}") String groupId,
            @Value("${spring.kafka.snapshot-consumer.key-deserializer}") String keyDeserializer,
            @Value("${spring.kafka.snapshot-consumer.value-deserializer}") String valueDeserializer,
            @Value("${spring.kafka.snapshot-consumer.enable-auto-commit}") boolean enableAutoCommit) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        this.consumer = new KafkaConsumer<>(props);
    }

    public ConsumerRecords<String, SensorsSnapshotAvro> poll(Duration timeout) {
        return consumer.poll(timeout);
    }

    public void commit() {
        consumer.commitSync();
    }

    public void close() {
        consumer.close();
    }

    public void subscribe(List<String> topics) {
        consumer.subscribe(topics);
    }

    public void wakeup() {
        consumer.wakeup();
    }

    @PreDestroy
    public void shutdown() {
        consumer.close();
    }
}