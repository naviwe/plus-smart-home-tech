package ru.yandex.practicum;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.shoppingstore.ProductDto;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface ShoppingStoreClient {
    @GetMapping("/{productId}")
    ProductDto getProductInfo(@PathVariable String productId);
}
