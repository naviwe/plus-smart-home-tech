package ru.yandex.practicum.telemetry.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.telemetry.model.enums.HubEventType;

@Getter
@Setter
@ToString(callSuper = true)
public class ScenarioRemovedEvent extends HubEvent {
    String name;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_REMOVED;
    }
}
