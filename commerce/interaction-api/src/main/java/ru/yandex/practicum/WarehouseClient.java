package ru.yandex.practicum;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.warehouse.ReserveProductsDto;
import ru.yandex.practicum.dto.shoppingcart.CartDto;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse")
public interface WarehouseClient {
    @PostMapping("/check")
    ReserveProductsDto checkAvailableProducts(@RequestBody CartDto shoppingCartDto);
}