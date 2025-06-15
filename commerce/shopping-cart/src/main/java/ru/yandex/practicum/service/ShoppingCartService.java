package ru.yandex.practicum.service;

import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.shoppingcart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.warehouse.ReserveProductsDto;
import ru.yandex.practicum.dto.shoppingcart.CartDto;

import java.util.Map;
import java.util.List;
import java.util.UUID;

public interface ShoppingCartService {
    CartDto getShoppingCart(String username);

    CartDto addProductsToCart(String username, Map<String, Long> items);

    void deleteUserCart(String username);

    @Transactional
    CartDto removeProductsFromCart(String username, List<UUID> productIds);

    CartDto changeCountProductInCart(String username, ChangeProductQuantityRequest request);

    ReserveProductsDto reserveProducts(String nameUser);
}
