package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;

public interface DeliveryService {
    DeliveryDto createNewDelivery(DeliveryDto deliveryDto);

    void setDeliverySuccess(String deliveryId);

    void setDeliveryPicked(String deliveryId);

    void setDeliveryFailed(String deliveryId);

    Double getCostDelivery(OrderDto orderDto);
}
