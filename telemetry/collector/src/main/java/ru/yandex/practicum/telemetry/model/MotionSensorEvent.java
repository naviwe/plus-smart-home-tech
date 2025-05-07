package ru.yandex.practicum.telemetry.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.telemetry.model.enums.SensorEventType;

@Getter
@Setter
@ToString(callSuper = true)
public class MotionSensorEvent extends SensorEvent {
    private Integer linkQuality;
    private Boolean motion;
    private Integer voltage;

    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}
