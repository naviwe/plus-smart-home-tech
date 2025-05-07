package ru.yandex.practicum.telemetry.dto;

import lombok.Data;
import ru.yandex.practicum.telemetry.config.DateConfig;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class ApiError {
    private List<String> errors;
    private String message;
    private String reason;
    private String status;
    private String timestamp;

    public ApiError(String status, String reason, Throwable ex) {
        this.errors = acceptStackTrace(ex);
        this.message = ex.getMessage();
        this.reason = reason;
        this.status = status;
        this.timestamp = LocalDateTime.now().format(DateConfig.FORMATTER);
    }

    private List<String> acceptStackTrace(Throwable ex) {
        return Stream.of(ex.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.toList());
    }
}