package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.dto.shoppingstore.ProductDto;
import ru.yandex.practicum.model.Product;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {
    Product productDtoToProduct(ProductDto productDto);

    ProductDto productToProductDto(Product product);
}