package ru.yandex.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.model.ShoppingCartItem;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ShoppingCartMapper {

    public ShoppingCartDto toDto(ShoppingCart cart) {
        return ShoppingCartDto.builder()
                .shoppingCartId(cart.getId())
                .products(mapItems(cart))
                .build();
    }

    private Map<UUID, Long> mapItems(ShoppingCart cart) {
        return cart.getItems().stream()
                .collect(Collectors.toMap(ShoppingCartItem::getProductId, ShoppingCartItem::getQuantity));
    }
}