package ru.yandex.practicum.telemetry.collector.handler.hub.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.telemetry.collector.handler.hub.GrpcHubEventHandler;
import ru.yandex.practicum.telemetry.collector.kafka.EventProducer;
import ru.yandex.practicum.telemetry.collector.mapper.GrpcHubEventMapper;

@Component
@RequiredArgsConstructor
public class DeviceRemovedGrpcHandler implements GrpcHubEventHandler {
    private final EventProducer producer;
    private static final String TOPIC = "telemetry.hubs.v1";

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED;
    }

    @Override
    public void handle(HubEventProto event) {
        var avro = GrpcHubEventMapper.toAvro(event);
        producer.send(TOPIC, event.getHubId(), avro);
    }
}
