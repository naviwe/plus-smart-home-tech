package ru.yandex.practicum;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.AssemblyProductsByOrder;
import ru.yandex.practicum.dto.ShippedForDelivery;
import ru.yandex.practicum.dto.shoppingcart.CartDto;
import ru.yandex.practicum.dto.warehouse.AddressWarehouseDto;
import ru.yandex.practicum.dto.warehouse.ReserveProductsDto;

import java.util.Map;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse")
public interface WarehouseClient {
    @PostMapping("/check")
    ReserveProductsDto checkAvailableProducts(@RequestBody CartDto shoppingCartDto);
    @PostMapping("/assembly")
    ReserveProductsDto reserveProducts(@RequestBody AssemblyProductsByOrder request);

    @GetMapping("/address")
    AddressWarehouseDto getAddressWarehouse();

    @PostMapping("/shipped")
    void shippedToDelivery(@RequestBody ShippedForDelivery deliveryRequest);

    @PostMapping("/return")
    void returnProductsToWarehouse(@RequestBody Map<String, Long> products);
}