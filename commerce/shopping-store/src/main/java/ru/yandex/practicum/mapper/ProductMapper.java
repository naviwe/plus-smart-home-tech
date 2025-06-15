package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.model.Product;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {

    Product productDtoToProduct(ProductDto productDto);

    ProductDto productToProductDto(Product product);

    List<ProductDto> mapListProducts(List<Product> products);
}
