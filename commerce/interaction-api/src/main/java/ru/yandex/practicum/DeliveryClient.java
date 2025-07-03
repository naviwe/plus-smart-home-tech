package ru.yandex.practicum;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;

@FeignClient(name = "delivery", path = "/api/v1/delivery")
public interface DeliveryClient {
    @PutMapping
    DeliveryDto createNewDelivery(@RequestBody DeliveryDto deliveryDto);

    @PostMapping("/cost")
    Double getCostDelivery(@RequestBody OrderDto orderDto);
}
