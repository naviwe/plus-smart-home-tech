package ru.yandex.practicum.aggregator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.aggregator.kafka.KafkaEventConsumer;
import ru.yandex.practicum.aggregator.kafka.KafkaEventProducer;
import ru.yandex.practicum.aggregator.services.SensorSnapshotService;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {

    private final KafkaEventConsumer consumer;
    private final KafkaEventProducer producer;
    private final SensorSnapshotService snapshotService;

    public void start() {
        log.info("Initializing subscription to topic telemetry.sensors.v1");
        consumer.subscribe(List.of("telemetry.sensors.v1"));

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
                                        "telemetry.snapshots.v1",
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