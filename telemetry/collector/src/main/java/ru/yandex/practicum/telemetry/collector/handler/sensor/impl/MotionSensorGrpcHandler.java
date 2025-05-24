package ru.yandex.practicum.telemetry.collector.handler.sensor.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.handler.sensor.GrpcSensorEventHandler;
import ru.yandex.practicum.telemetry.collector.kafka.EventProducer;
import ru.yandex.practicum.telemetry.collector.mapper.GrpcSensorEventMapper;

@Component
@RequiredArgsConstructor
public class MotionSensorGrpcHandler implements GrpcSensorEventHandler {

    private final EventProducer producer;
    private static final String TOPIC = "telemetry.sensors.v1";

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto event) {
        var avro = GrpcSensorEventMapper.toAvro(event);
        producer.send(TOPIC, event.getId(), avro);
    }
}
