package ru.yandex.practicum.telemetry.controller;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final String error;
    private final String details;

    public ErrorResponse(String error, String details) {
        this.error = error;
        this.details = details;
    }

}