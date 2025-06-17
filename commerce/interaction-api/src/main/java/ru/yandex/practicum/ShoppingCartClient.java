package ru.yandex.practicum;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.shoppingcart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.shoppingcart.CartDto;
import ru.yandex.practicum.dto.warehouse.ReserveProductsDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "shopping-cart-service", url = "${shopping-cart.service.url}", path = "/api/v1/shopping-cart")
public interface ShoppingCartClient {

    @GetMapping
    CartDto getShoppingCart(@RequestParam String username);

    @PutMapping
    CartDto addProductsToCart(
            @RequestParam String username,
            @RequestBody Map<String, Long> items);

    @DeleteMapping
    void deleteUserCart(@RequestParam String username);

    @PostMapping("/remove")
    CartDto removeFromShoppingCart(
            @RequestParam String username,
            @RequestBody List<UUID> productIds);

    @PostMapping("/change-quantity")
    CartDto changeCountProductsOfCart(
            @RequestParam String username,
            @RequestBody ChangeProductQuantityRequest request);

    @PostMapping("/booking")
    ReserveProductsDto reserveProducts(@RequestParam String nameUser);
}