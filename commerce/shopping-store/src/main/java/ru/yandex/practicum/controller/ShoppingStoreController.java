package ru.yandex.practicum.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.shoppingstore.Pageable;
import ru.yandex.practicum.dto.shoppingstore.ProductDto;
import ru.yandex.practicum.dto.shoppingstore.SetProductCountState;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.enums.QuantityState;
import ru.yandex.practicum.service.ShoppingStoreService;

import jakarta.validation.Valid;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-store")
public class ShoppingStoreController {
    private final ShoppingStoreService storeService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Page<ProductDto> getProductsByCategory(
            @RequestParam @NotNull ProductCategory category,
            @Valid Pageable pageable) {
        log.info("Request to retrieve product list by category {}", category);
        return storeService.getProductsByCategory(category, pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public ProductDto createProduct(@RequestBody ProductDto productDto) {
        log.info("Request to create a new product {}", productDto);
        return storeService.createProduct(productDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public ProductDto updateProduct(@RequestBody ProductDto productDto) {
        log.info("Request to update product {}", productDto);
        return storeService.updateProduct(productDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/removeProductFromStore")
    public boolean removeProduct(@RequestBody String productId) {
        String cleanedProductId = productId.replaceAll("^\"|\"$", "");
        log.info("Request to remove product {}", cleanedProductId);
        return storeService.removeProduct(cleanedProductId);
    }

    @PostMapping("/quantityState")
    public boolean changeState(
            @RequestParam String productId,
            @RequestParam QuantityState quantityState) {
        log.info("Request to change quantity state of product {} to {}", productId, quantityState);
        SetProductCountState request = new SetProductCountState(productId, quantityState);
        return storeService.changeState(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{productId}")
    public ProductDto getInfoByProduct(@PathVariable String productId) {
        log.info("Request to get information about product {}", productId);
        return storeService.getInfoByProduct(productId);
    }
}