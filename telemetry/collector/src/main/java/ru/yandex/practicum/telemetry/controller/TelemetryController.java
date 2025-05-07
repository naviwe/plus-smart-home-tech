package ru.yandex.practicum.telemetry.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.telemetry.model.HubEvent;
import ru.yandex.practicum.telemetry.model.SensorEvent;
import ru.yandex.practicum.telemetry.service.TelemetryService;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class TelemetryController {
    private final TelemetryService telemetryService;

    @PostMapping("/sensors")
    @ResponseStatus(HttpStatus.OK)
    public void collectSensorEvent(@Valid @RequestBody SensorEvent event) {
        telemetryService.processSensorEvent(event);
    }

    @PostMapping("/hubs")
    @ResponseStatus(HttpStatus.OK)
    public void collectHubEvent(@Valid @RequestBody HubEvent event) {
        telemetryService.processHubEvent(event);
    }
}