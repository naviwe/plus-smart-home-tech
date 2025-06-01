package ru.yandex.practicum.aggregator;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.aggregator.kafka.KafkaEventConsumer;
import ru.yandex.practicum.aggregator.kafka.KafkaEventProducer;
import ru.yandex.practicum.aggregator.services.SensorSnapshotService;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
public class AggregationStarter {

    private final KafkaEventConsumer consumer;
    private final KafkaEventProducer producer;
    private final SensorSnapshotService snapshotService;
    private final String sensorsTopic;
    private final String snapshotsTopic;

    public AggregationStarter(
            KafkaEventConsumer consumer,
            KafkaEventProducer producer,
            SensorSnapshotService snapshotService,
            @Value("${kafka.topics.sensors}") String sensorsTopic,
            @Value("${kafka.topics.snapshots}") String snapshotsTopic) {
        this.consumer = consumer;
        this.producer = producer;
        this.snapshotService = snapshotService;
        this.sensorsTopic = sensorsTopic;
        this.snapshotsTopic = snapshotsTopic;

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutdown hook triggered, waking up consumer...");
            consumer.wakeup();
        }));
    }

    public void start() {
        log.info("Initializing subscription to topic {}", sensorsTopic);
        consumer.subscribe(List.of(sensorsTopic));

        try {
            while (true) {
                log.debug("Waiting for new Kafka messages...");
                var records = consumer.poll(Duration.ofMillis(100));
                log.debug("Received {} messages", records.count());
                for (var record : records) {
                    var event = record.value();
                    log.debug("Processing sensor event: {}", event);
                    try {
                        snapshotService.updateSnapshot(event).ifPresent(snapshot -> {
                            try {
                                producer.send(
                                        snapshotsTopic,
                                        snapshot.getHubId(),
                                        snapshot
                                );
                                log.info("Sent hub snapshot: {}", snapshot.getHubId());
                            } catch (Exception e) {
                                log.error("Error sending snapshot to Kafka: hubId={}", snapshot.getHubId(), e);
                            }
                        });
                    } catch (Exception e) {
                        log.error("Error updating snapshot for event: {}", event, e);
                    }
                }
                try {
                    consumer.commit();
                    log.debug("Offsets committed successfully");
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
                producer.flush();
                log.info("Producer flushed pending data");

                consumer.commit();
                log.info("Final offsets commit completed");

            } catch (Exception e) {
                log.error("Error during final resource cleanup", e);
            } finally {
                try {
                    log.info("Closing producer...");
                    producer.close();
                    log.info("Producer closed successfully");
                } catch (Exception e) {
                    log.error("Error closing producer", e);
                }
                try {
                    log.info("Closing consumer...");
                    consumer.close();
                    log.info("Consumer closed successfully");
                } catch (Exception e) {
                    log.error("Error closing consumer", e);
                }
            }
        }
    }
}