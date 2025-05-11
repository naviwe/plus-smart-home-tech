package ru.yandex.practicum.telemetry.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "kafka")
public class KafkaConfig {
    private String bootstrapServers;
    private Producer producer;
    private Topics topics;

    @Getter
    @Setter
    public static class Producer {
        private String keySerializer;
        private String valueSerializer;
    }

    @Getter
    @Setter
    public static class Topics {
        private String sensor;
        private String hub;
    }
}