package ru.yandex.practicum.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.dto.ApiError;
import ru.yandex.practicum.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.mapper.ApiErrorResponseMapper;

@RestControllerAdvice(basePackages = "com.example.warehouse")
public class WarehouseExceptionHandler {

    @ExceptionHandler(SpecifiedProductAlreadyInWarehouseException.class)
    public ResponseEntity<ApiError> handleAlreadyExists(SpecifiedProductAlreadyInWarehouseException ex) {
        ApiError error = ApiErrorResponseMapper.toApiError(ex, HttpStatus.BAD_REQUEST, "Товар уже зарегистрирован на складе");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(NoSpecifiedProductInWarehouseException.class)
    public ResponseEntity<ApiError> handleNotFound(NoSpecifiedProductInWarehouseException ex) {
        ApiError error = ApiErrorResponseMapper.toApiError(ex, HttpStatus.BAD_REQUEST, "Товар не найден на складе");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ProductInShoppingCartLowQuantityInWarehouse.class)
    public ResponseEntity<ApiError> handleLowQuantity(ProductInShoppingCartLowQuantityInWarehouse ex) {
        ApiError error = ApiErrorResponseMapper.toApiError(ex, HttpStatus.BAD_REQUEST, "Недостаточное количество товара на складе");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(Exception ex) {
        ApiError error = ApiErrorResponseMapper.toApiError(ex, HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка на складе");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}