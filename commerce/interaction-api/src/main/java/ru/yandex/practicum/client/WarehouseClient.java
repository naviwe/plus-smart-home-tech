package ru.yandex.practicum.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse")
public interface WarehouseClient {

    @GetMapping("/address")
    AddressDto getWarehouseAddress();

    @PostMapping("/check")
    BookedProductsDto checkProductAvailability(@RequestBody ShoppingCartDto cart);

    @PostMapping("/add")
    void addProductQuantity(@RequestBody AddProductToWarehouseRequest request);

    @PutMapping
    void registerNewProduct(@RequestBody NewProductInWarehouseRequest request);
}