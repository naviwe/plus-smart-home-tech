package ru.yandex.practicum.exception;

import lombok.Getter;

@Getter
public class SpecifiedProductAlreadyInWarehouseException extends RuntimeException {

    public SpecifiedProductAlreadyInWarehouseException(String message) {
        super(message);
    }

    public SpecifiedProductAlreadyInWarehouseException(String message, Throwable cause) {
        super(message, cause);
    }
}