package ru.yandex.practicum.telemetry.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.telemetry.model.HubEvent;
import ru.yandex.practicum.telemetry.model.SensorEvent;
import ru.yandex.practicum.telemetry.service.TelemetryService;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class TelemetryController {
    private static final Logger logger = LoggerFactory.getLogger(TelemetryController.class);
    private final TelemetryService telemetryService;

    @PostMapping("/sensors")
    @ResponseStatus(HttpStatus.OK)
    public void collectSensorEvent(@Valid @RequestBody SensorEvent event) {
        logger.info("Received sensor event: id={}, type={}", event.getId(), event.getType());
        telemetryService.processSensorEvent(event);
        logger.debug("Sensor event processed successfully: id={}", event.getId());
    }

    @PostMapping("/hubs")
    @ResponseStatus(HttpStatus.OK)
    public void collectHubEvent(@Valid @RequestBody HubEvent event) {
        logger.info("Received hub event: hubId={}, type={}", event.getHubId(), event.getType());
        telemetryService.processHubEvent(event);
        logger.debug("Hub event processed successfully: hubId={}", event.getHubId());
    }
}