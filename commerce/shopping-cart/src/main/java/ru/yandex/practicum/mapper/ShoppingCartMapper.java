package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.dto.shoppingcart.CartDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ShoppingCartMapper {
    @Mapping(target = "shoppingCartId", source = "shoppingCartId")
    @Mapping(target = "products", source = "products")
    CartDto toShoppingCartDto(ShoppingCart shoppingCart);
}