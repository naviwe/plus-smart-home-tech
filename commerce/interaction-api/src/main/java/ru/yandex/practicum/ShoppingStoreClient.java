package ru.yandex.practicum;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.shoppingstore.Pageable;
import ru.yandex.practicum.dto.shoppingstore.ProductDto;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.enums.QuantityState;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface ShoppingStoreClient {

    @GetMapping
    Page<ProductDto> getProductsByCategory(
            @RequestParam ProductCategory category,
            @RequestBody Pageable pageable);

    @PutMapping
    ProductDto createProduct(@RequestBody ProductDto productDto);

    @PostMapping
    ProductDto updateProduct(@RequestBody ProductDto productDto);

    @PostMapping("/removeProductFromStore")
    boolean removeProduct(@RequestBody String productId);

    @PostMapping("/quantityState")
    boolean changeState(
            @RequestParam String productId,
            @RequestParam QuantityState quantityState);

    @GetMapping("/{productId}")
    ProductDto getInfoByProduct(@PathVariable String productId);
}
