package ru.yandex.practicum.telemetry.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.telemetry.model.HubEvent;
import ru.yandex.practicum.telemetry.model.SensorEvent;

@Service
@RequiredArgsConstructor
public class TelemetryService {

    public void processSensorEvent(SensorEvent event) {

    }

    public void processHubEvent(HubEvent event) {

    }
}