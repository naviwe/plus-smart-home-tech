package ru.yandex.practicum.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.store.ChangeProductQuantityRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(
        name = "shopping-cart",
        path = "/api/v1/shopping-cart"
)
public interface ShoppingCartClient {

    @GetMapping
    ShoppingCartDto getCart(@RequestParam("username") String username);

    @PutMapping
    ShoppingCartDto addProducts(
            @RequestParam("username") String username,
            @RequestBody Map<UUID, Long> productsToAdd
    );

    @PostMapping("/change-quantity")
    ShoppingCartDto changeQuantity(
            @RequestParam("username") String username,
            @RequestBody ChangeProductQuantityRequest request
    );

    @PostMapping("/remove")
    ShoppingCartDto removeProducts(
            @RequestParam("username") String username,
            @RequestBody List<UUID> productIdsToRemove
    );

    @DeleteMapping
    void deactivateCart(@RequestParam("username") String username);
}