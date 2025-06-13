package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.Pageable;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.SetProductCountState;
import ru.yandex.practicum.enums.ProductCategory;

import java.util.List;

public interface ShoppingStoreService {
    List<ProductDto> getProductsByCategory(ProductCategory category, Pageable pageable);

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    boolean removeProduct(String productId);

    boolean changeState(SetProductCountState request);

    ProductDto getInfoByProduct(String productId);
}
