package ru.yandex.practicum.telemetry.mapper;

import com.example.avro.*;
import ru.yandex.practicum.telemetry.model.*;

import java.util.List;

public class HubEventAvroMapper {

    public static HubEventAvro mapSensorEvent(HubEvent event) {
        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(mapPayload(event))
                .build();
    }

    private static Object mapPayload(HubEvent event) {
        return switch (event.getType()) {
            case DEVICE_ADDED -> mapPayload((DeviceAddedEvent) event);
            case DEVICE_REMOVED -> mapPayload((DeviceRemovedEvent) event);
            case SCENARIO_ADDED -> mapPayload((ScenarioAddedEvent) event);
            case SCENARIO_REMOVED -> mapPayload((ScenarioRemovedEvent) event);
        };
    }

    private static DeviceAddedEventAvro mapPayload(DeviceAddedEvent event) {
        return DeviceAddedEventAvro.newBuilder()
                .setId(event.getId())
                .setType(DeviceTypeAvro.valueOf(event.getDeviceType().name()))
                .build();
    }

    private static DeviceRemovedEventAvro mapPayload(DeviceRemovedEvent event) {
        return DeviceRemovedEventAvro.newBuilder()
                .setId(event.getId())
                .build();
    }

    private static ScenarioAddedEventAvro mapPayload(ScenarioAddedEvent event) {
        return ScenarioAddedEventAvro.newBuilder()
                .setActions(mapActions(event.getActions()))
                .setConditions(mapConditions(event.getConditions()))
                .setName(event.getName())
                .build();
    }

    private static ScenarioRemovedEventAvro mapPayload(ScenarioRemovedEvent event) {
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(event.getName())
                .build();
    }

    private static List<DeviceActionAvro> mapActions(List<DeviceAction> deviceActions) {
        return deviceActions.stream()
                .map(deviceAction -> DeviceActionAvro.newBuilder()
                        .setType(ActionTypeAvro.valueOf(deviceAction.getType().name()))
                        .setSensorId(deviceAction.getSensorId())
                        .setValue(deviceAction.getValue())
                        .build())
                .toList();
    }

    private static List<ScenarioConditionAvro> mapConditions(List<ScenarioCondition> scenarioConditions) {
        return scenarioConditions.stream()
                .map(scenarioCondition -> ScenarioConditionAvro.newBuilder()
                        .setType(ConditionTypeAvro.valueOf(scenarioCondition.getType().name()))
                        .setOperation(ConditionOperationAvro.valueOf(scenarioCondition.getOperation().name()))
                        .setSensorId(scenarioCondition.getSensorId())
                        .setValue(scenarioCondition.getValue())
                        .build())
                .toList();
    }

}