package ru.yandex.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.store.ProductDto;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.model.store.ProductCategory;
import ru.yandex.practicum.model.store.ProductState;
import ru.yandex.practicum.model.store.QuantityState;

import java.util.Objects;

@Component
public class ProductMapper {

    public ProductDto toDto(Product product) {
        if (Objects.isNull(product)) {
            return null;
        }

        return ProductDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .imageSrc(product.getImageSrc())
                .price(product.getPrice())
                .quantityState(product.getQuantityState().name())
                .productState(product.getProductState().name())
                .productCategory(product.getProductCategory().name())
                .build();
    }

    public Product toEntity(ProductDto dto) {
        if (Objects.isNull(dto)) {
            return null;
        }

        Product product = new Product();
        product.setProductId(dto.getProductId());
        product.setProductName(dto.getProductName());
        product.setDescription(dto.getDescription());
        product.setImageSrc(dto.getImageSrc());
        product.setPrice(dto.getPrice());

        if (dto.getQuantityState() != null) {
            product.setQuantityState(QuantityState.valueOf(dto.getQuantityState()));
        }

        if (dto.getProductState() != null) {
            product.setProductState(ProductState.valueOf(dto.getProductState()));
        }

        if (dto.getProductCategory() != null) {
            product.setProductCategory(ProductCategory.valueOf(dto.getProductCategory()));
        }

        return product;
    }
}