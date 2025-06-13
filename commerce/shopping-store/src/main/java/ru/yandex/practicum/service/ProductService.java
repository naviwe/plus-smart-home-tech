package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.Pageable;
import ru.yandex.practicum.dto.store.ProductDto;
import ru.yandex.practicum.dto.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.model.store.ProductCategory;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    List<ProductDto> getProductsByCategory(ProductCategory category, Pageable pageable);

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    boolean removeProduct(UUID productId);

    boolean setProductQuantityState(SetProductQuantityStateRequest request);

    ProductDto getProductById(UUID productId);
}