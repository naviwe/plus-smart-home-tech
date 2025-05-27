package ru.yandex.practicum.aggregator.kafka;

import jakarta.annotation.PreDestroy;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.practicum.telemetry.serialization.AvroSerializer;

import java.util.Properties;
import java.util.concurrent.Future;

@Component
public class KafkaEventProducer {

    private final KafkaProducer<String, SpecificRecordBase> producer;

    public KafkaEventProducer(
            @Value("${spring.kafka.producer.bootstrap-servers}") String bootstrapServers,
            @Value("${spring.kafka.producer.key-serializer}") String keySerializer,
            @Value("${spring.kafka.producer.value-serializer}") String valueSerializer) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        this.producer = new KafkaProducer<>(props);
    }

    public Future<RecordMetadata> send(String topic, String key, SpecificRecordBase value) {
        return producer.send(new ProducerRecord<>(topic, key, value));
    }

    public void flush() {
        producer.flush();
    }

    public void close() {
        producer.close();
    }

    @PreDestroy
    public void shutdown() {
        producer.flush();
        producer.close();
    }
}