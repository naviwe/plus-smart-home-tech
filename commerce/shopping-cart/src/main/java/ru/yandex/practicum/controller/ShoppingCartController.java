package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.shoppingcart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.shoppingcart.RemoveProductsRequest;
import ru.yandex.practicum.service.ShoppingCartService;
import ru.yandex.practicum.dto.shoppingcart.CartDto;
import ru.yandex.practicum.dto.warehouse.ReserveProductsDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-cart")
public class ShoppingCartController {
    private final ShoppingCartService cartService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public CartDto getShoppingCart(@RequestParam String username) {
        log.info("Запрос на получение корзины пользователя {}", username);
        return cartService.getShoppingCart(username);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public CartDto addProductsToCart(@RequestParam String username,
                                     @RequestBody Map<String, Long> items) {
        log.info("Запрос на добавление вещей {} в корзину пользователя {}", items, username);

        return cartService.addProductsToCart(username, items);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping
    public void deleteUserCart(@RequestParam String username) {
        log.info("Запрос на деактивацию корзины товаров для пользователя {}", username);
        cartService.deleteUserCart(username);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/remove")
    public CartDto removeFromShoppingCart(
            @RequestParam String username,
            @Valid @RequestBody List<UUID> productIds) {
        log.info("Запрос на удаление товаров {} из корзины пользователя {}", productIds, username);
        return cartService.removeProductsFromCart(username, productIds);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/change-quantity")
    public CartDto changeCountProductsOfCart(@RequestParam String username,
                                             @RequestBody ChangeProductQuantityRequest request) {
        log.info("Запрос на изменение количества товаров {} в корзине пользователя {}", request, username);
        return cartService.changeCountProductInCart(username, request);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/booking")
    public ReserveProductsDto reserveProducts(@RequestParam String nameUser) {
        log.info("Запрос на зарезервирование товаров на складе для пользователя {}", nameUser);
        return cartService.reserveProducts(nameUser);
    }
}
