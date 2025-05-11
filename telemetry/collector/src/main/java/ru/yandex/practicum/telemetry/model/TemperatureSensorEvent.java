package ru.yandex.practicum.telemetry.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.telemetry.model.enums.SensorEventType;

@Getter
@Setter
@ToString(callSuper = true)
public class TemperatureSensorEvent extends SensorEvent {
    private int temperatureC;
    private int temperatureF;

    @Override
    public SensorEventType getType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }
}
