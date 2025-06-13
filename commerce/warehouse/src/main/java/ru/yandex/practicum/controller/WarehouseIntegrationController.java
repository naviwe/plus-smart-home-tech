package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.service.WarehouseService;

@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseIntegrationController {

    private final WarehouseService warehouseService;

    @PostMapping("/check")
    public ResponseEntity<BookedProductsDto> checkAvailability(@RequestBody ShoppingCartDto cart) {
        BookedProductsDto result = warehouseService.checkProductAvailability(cart);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/address")
    public ResponseEntity<AddressDto> getWarehouseAddress() {
        return ResponseEntity.ok(warehouseService.getWarehouseAddress());
    }
}