package ru.yandex.practicum.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.dto.ApiError;
import ru.yandex.practicum.exception.NoProductsInShoppingCartException;
import ru.yandex.practicum.exception.NotAuthorizedUserException;
import ru.yandex.practicum.mapper.ApiErrorResponseMapper;

@RestControllerAdvice
public class CartExceptionHandler {

    @ExceptionHandler(NotAuthorizedUserException.class)
    public ResponseEntity<ApiError> handleUnauthorizedUser(NotAuthorizedUserException ex) {
        ApiError error = ApiErrorResponseMapper.toApiError(ex, HttpStatus.UNAUTHORIZED, "Пользователь не авторизован");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(NoProductsInShoppingCartException.class)
    public ResponseEntity<ApiError> handleEmptyCart(NoProductsInShoppingCartException ex) {
        ApiError error = ApiErrorResponseMapper.toApiError(ex, HttpStatus.BAD_REQUEST, "Нет запрошенных товаров в корзине");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(Exception ex) {
        ApiError error = ApiErrorResponseMapper.toApiError(ex, HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка в модуле корзины");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}