package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.Pageable;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.SetProductCountState;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.service.ShoppingStoreService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-store")
public class ShoppingStoreController {
    private final ShoppingStoreService storeService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ProductDto> getProductsByCategory(@RequestParam ProductCategory category, Pageable pageable) {
        log.info("Запрос на получение списка товаров по категории {} и страницам {}", category, pageable);
        return storeService.getProductsByCategory(category, pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    public List<ProductDto> getAllProducts(Pageable pageable) {
        log.info("Запрос на получение всех товаров с пагинацией {}", pageable);
        return storeService.getAllProducts(pageable);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ProductDto createProduct(@Valid @RequestBody ProductDto productDto) {
        log.info("Запрос на создание нового товара {}", productDto);
        return storeService.createProduct(productDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public ProductDto updateProduct(@Valid @RequestBody ProductDto productDto) {
        log.info("Запрос на обновление товара {}", productDto);
        return storeService.updateProduct(productDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/removeProductFromStore")
    public void removeProduct(@RequestParam @NotBlank String productId) {
        log.info("Запрос на удаление товара {}", productId);
        storeService.removeProduct(productId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/quantityState")
    public void changeState(@Valid @RequestBody SetProductCountState request) {
        log.info("Запрос на установку статуса для товара {}", request);
        storeService.changeState(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{productId}")
    public ProductDto getInfoByProduct(@PathVariable String productId) {
        log.info("Запрос на получение информации о товаре {}", productId);
        return storeService.getInfoByProduct(productId);
    }
}