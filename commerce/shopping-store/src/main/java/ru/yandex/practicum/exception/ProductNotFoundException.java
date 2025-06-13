package ru.yandex.practicum.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ProductNotFoundException extends RuntimeException {
    private HttpStatus httpStatus;
    private String userMessage;

    public ProductNotFoundException(String message, String userMessage, HttpStatus httpStatus) {
        super(message);
        this.userMessage = userMessage;
        this.httpStatus = httpStatus;
    }
}