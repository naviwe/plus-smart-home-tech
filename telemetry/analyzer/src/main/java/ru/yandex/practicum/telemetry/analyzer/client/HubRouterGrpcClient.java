package ru.yandex.practicum.telemetry.analyzer.client;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.telemetry.analyzer.mapper.GrpcDeviceActionMapper;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HubRouterGrpcClient {

    @GrpcClient("hub-router")
    HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterStub;
    final GrpcDeviceActionMapper grpcDeviceActionMapper;

    public void sendDeviceAction(String hubId, String scenarioName, String sensorId, Integer value, String type) {
        try {
            log.info("Sending device command via gRPC: hubId={}, scenario={}, sensorId={}, value={}, type={}",
                    hubId, scenarioName, sensorId, value, type);
            DeviceActionProto action = grpcDeviceActionMapper.toDeviceAction(sensorId, type, value);
            DeviceActionRequest request = grpcDeviceActionMapper.toDeviceActionRequest(hubId, scenarioName, action);
            log.warn("Conditions met, sending command: {}", action);
            hubRouterStub.handleDeviceAction(request);
            log.info("Command successfully sent via gRPC");
        } catch (Exception e) {
            log.error("Error sending command to gRPC service: {}", e.getMessage(), e);
        }
    }
}