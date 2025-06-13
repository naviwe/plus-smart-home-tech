package ru.yandex.practicum.mapper;

import org.springframework.http.HttpStatus;
import ru.yandex.practicum.dto.ApiError;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ApiErrorResponseMapper {

    public static ApiError toApiError(Exception ex, HttpStatus status, String userMessage) {
        return ApiError.builder()
                .httpStatus(status.value() + " " + status.name())
                .message(ex.getMessage())
                .userMessage(userMessage)
                .localizedMessage(ex.getLocalizedMessage())
                .cause(buildCause(ex.getCause()))
                .stackTrace(convertStackTrace(ex.getStackTrace()))
                .suppressed(convertSuppressed(ex.getSuppressed()))
                .build();
    }

    private static ApiError buildCause(Throwable cause) {
        if (Objects.isNull(cause)) return null;

        return ApiError.builder()
                .message(cause.getMessage())
                .localizedMessage(cause.getLocalizedMessage())
                .stackTrace(convertStackTrace(cause.getStackTrace()))
                .suppressed(convertSuppressed(cause.getSuppressed()))
                .build();
    }

    private static List<StackTraceElement> convertStackTrace(StackTraceElement[] elements) {
        return elements != null ? Arrays.asList(elements) : null;
    }

    private static List<ApiError> convertSuppressed(Throwable[] suppressed) {
        if (Objects.isNull(suppressed)) return null;

        return Arrays.stream(suppressed)
                .map(s -> ApiError.builder()
                        .message(s.getMessage())
                        .localizedMessage(s.getLocalizedMessage())
                        .stackTrace(convertStackTrace(s.getStackTrace()))
                        .build())
                .collect(Collectors.toList());
    }
}