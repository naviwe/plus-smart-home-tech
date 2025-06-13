package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.store.ChangeProductQuantityRequest;
import ru.yandex.practicum.service.ShoppingCartService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-cart")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @GetMapping
    public ResponseEntity<ShoppingCartDto> getCart(@RequestParam String username) {
        return ResponseEntity.ok(shoppingCartService.getCart(username));
    }

    @PutMapping
    public ResponseEntity<ShoppingCartDto> addProducts(
            @RequestParam String username,
            @RequestBody Map<UUID, Long> productsToAdd) {
        return ResponseEntity.ok(shoppingCartService.addProducts(username, productsToAdd));
    }

    @PostMapping("/change-quantity")
    public ResponseEntity<ShoppingCartDto> changeQuantity(
            @RequestParam String username,
            @RequestBody ChangeProductQuantityRequest request) {
        return ResponseEntity.ok(shoppingCartService.changeProductQuantity(username, request));
    }

    @PostMapping("/remove")
    public ResponseEntity<ShoppingCartDto> removeProducts(
            @RequestParam String username,
            @RequestBody List<UUID> productIdsToRemove) {
        return ResponseEntity.ok(shoppingCartService.removeProducts(username, productIdsToRemove));
    }

    @DeleteMapping
    public ResponseEntity<Void> deactivateCart(@RequestParam String username) {
        shoppingCartService.deactivateCart(username);
        return ResponseEntity.ok().build();
    }
}