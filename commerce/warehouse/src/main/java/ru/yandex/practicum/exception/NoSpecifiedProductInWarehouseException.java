package ru.yandex.practicum.exception;

import lombok.Getter;

@Getter
public class NoSpecifiedProductInWarehouseException extends RuntimeException {

    public NoSpecifiedProductInWarehouseException(String message) {
        super(message);
    }

    public NoSpecifiedProductInWarehouseException(String message, Throwable cause) {
        super(message, cause);
    }
}