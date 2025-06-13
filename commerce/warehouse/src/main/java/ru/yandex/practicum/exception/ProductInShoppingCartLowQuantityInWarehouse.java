package ru.yandex.practicum.exception;

import lombok.Getter;

@Getter
public class ProductInShoppingCartLowQuantityInWarehouse extends RuntimeException {

    public ProductInShoppingCartLowQuantityInWarehouse(String message) {
        super(message);
    }

    public ProductInShoppingCartLowQuantityInWarehouse(String message, Throwable cause) {
        super(message, cause);
    }
}