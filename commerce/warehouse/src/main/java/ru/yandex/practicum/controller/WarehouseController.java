package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.shoppingcart.CartDto;
import ru.yandex.practicum.dto.warehouse.AddProductInWarehouse;
import ru.yandex.practicum.dto.warehouse.AddressWarehouseDto;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouse;
import ru.yandex.practicum.dto.warehouse.ReserveProductsDto;
import ru.yandex.practicum.service.WarehouseService;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouse")
public class WarehouseController {
    private final WarehouseService warehouseService;

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public void createProductInWarehouse(@RequestBody NewProductInWarehouse request) {
        log.info("Request to add a new product to the warehouse: {}", request);
        warehouseService.createProductInWarehouse(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/check")
    public ReserveProductsDto checkCountProducts(@RequestBody CartDto cartDto) {
        log.info("Request to check quantity of products in the warehouse: {}", cartDto);
        return warehouseService.checkCountProducts(cartDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/add")
    public void addProductInWarehouse(@RequestBody AddProductInWarehouse request) {
        log.info("Request to add products to the warehouse: {}", request);
        warehouseService.addProductInWarehouse(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/address")
    public AddressWarehouseDto getAddressWarehouse() {
        log.info("Request to get the warehouse address for delivery calculation.");
        return warehouseService.getAddressWarehouse();
    }
}