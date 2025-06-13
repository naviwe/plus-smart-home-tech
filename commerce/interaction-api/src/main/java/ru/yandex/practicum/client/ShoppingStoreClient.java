package ru.yandex.practicum.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.Pageable;
import ru.yandex.practicum.dto.store.ProductDto;
import ru.yandex.practicum.dto.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.model.store.ProductCategory;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface ShoppingStoreClient {

    @GetMapping
    List<ProductDto> getProducts(@RequestParam("category") ProductCategory category,
                                 @SpringQueryMap Pageable pageable);

    @PutMapping
    ProductDto createNewProduct(@RequestBody ProductDto productDto);

    @PostMapping
    ProductDto updateProduct(@RequestBody ProductDto productDto);

    @PostMapping("/removeProductFromStore")
    Boolean removeProductFromStore(@RequestBody UUID productId);

    @PostMapping("/quantityState")
    Boolean setProductQuantityState(@RequestBody SetProductQuantityStateRequest request);

    @GetMapping("/{productId}")
    ProductDto getProduct(@PathVariable UUID productId);
}