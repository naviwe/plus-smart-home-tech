package ru.yandex.practicum.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    private String httpStatus;
    private String message;
    private String userMessage;
    private String localizedMessage;
    private ApiError cause;
    private List<StackTraceElement> stackTrace;
    private List<ApiError> suppressed;

}