package ru.yandex.practicum.telemetry.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.telemetry.model.enums.SensorEventType;

@Getter
@Setter
@ToString(callSuper = true)
public class ClimateSensorEvent extends SensorEvent {
    private int temperatureC;
    private int humidity;
    private int co2Level;

    @Override
    public SensorEventType getType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }
}
