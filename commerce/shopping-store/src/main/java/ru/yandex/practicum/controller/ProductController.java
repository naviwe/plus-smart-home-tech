package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.client.ShoppingStoreClient;
import ru.yandex.practicum.dto.Pageable;
import ru.yandex.practicum.dto.store.ProductDto;
import ru.yandex.practicum.dto.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.model.store.ProductCategory;
import ru.yandex.practicum.service.ProductService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ShoppingStoreClient client;

    @GetMapping
    public List<ProductDto> getProducts(@RequestParam("category") ProductCategory category,
                                        @SpringQueryMap Pageable pageable) {
        return productService.getProductsByCategory(category, pageable);
    }

    @PutMapping
    public ResponseEntity<ProductDto> createNewProduct(@RequestBody @Valid ProductDto productDto) {
        ProductDto created = productService.createProduct(productDto);
        return ResponseEntity.ok(created);
    }

    @PostMapping
    public ResponseEntity<ProductDto> updateProduct(@RequestBody @Valid ProductDto productDto) {
        ProductDto updated = productService.updateProduct(productDto);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/removeProductFromStore")
    public ResponseEntity<Boolean> removeProductFromStore(@RequestBody UUID productId) {
        boolean result = productService.removeProduct(productId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/quantityState")
    public ResponseEntity<Boolean> setProductQuantityState(@RequestBody SetProductQuantityStateRequest request) {
        boolean result = productService.setProductQuantityState(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable UUID productId) {
        ProductDto product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }
}
