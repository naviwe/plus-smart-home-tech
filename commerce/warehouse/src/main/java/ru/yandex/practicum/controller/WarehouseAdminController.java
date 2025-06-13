package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.service.WarehouseService;

@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseAdminController {

    private final WarehouseService warehouseService;

    @PutMapping
    public ResponseEntity<Void> registerNewProduct(@RequestBody NewProductInWarehouseRequest request) {
        warehouseService.registerNewProduct(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addProductQuantity(@RequestBody AddProductToWarehouseRequest request) {
        warehouseService.addProductQuantity(request);
        return ResponseEntity.ok().build();
    }
}