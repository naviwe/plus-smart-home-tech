package ru.yandex.practicum.telemetry.collector.handler.hub;

import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

public interface GrpcHubEventHandler {
    HubEventProto.PayloadCase getMessageType();
    void handle(HubEventProto event);
}