package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.shoppingstore.Pageable;
import ru.yandex.practicum.dto.shoppingstore.ProductDto;
import ru.yandex.practicum.dto.shoppingstore.SetProductCountState;
import ru.yandex.practicum.enums.ProductCategory;
import org.springframework.data.domain.Page;

public interface ShoppingStoreService {
    Page<ProductDto> getProductsByCategory(ProductCategory category, Pageable pageable);

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    boolean removeProduct(String productId);

    boolean changeState(SetProductCountState request);

    ProductDto getInfoByProduct(String productId);
}