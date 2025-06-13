package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.store.ChangeProductQuantityRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ShoppingCartService {

    ShoppingCartDto getCart(String username);

    ShoppingCartDto addProducts(String username, Map<UUID, Long> productsToAdd);

    ShoppingCartDto removeProducts(String username, List<UUID> productIds);

    ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest request);

    void deactivateCart(String username);
}