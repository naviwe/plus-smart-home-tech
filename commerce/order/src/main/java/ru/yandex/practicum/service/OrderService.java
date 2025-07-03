package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.NewOrder;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.ReturnProduct;

import java.util.List;

public interface OrderService {
    List<OrderDto> getUserOrders(String username);

    OrderDto createNewOrder(NewOrder request);

    OrderDto returnOrder(ReturnProduct request);

    OrderDto paymentOrder(String orderId);

    OrderDto paymentOrderWithError(String orderId);

    OrderDto deliveryOrder(String orderId);

    OrderDto deliveryOrderWithError(String orderId);

    OrderDto completedOrder(String orderId);

    OrderDto calculateTotalOrder(String orderId);

    OrderDto calculateDeliveryOrder(String orderId);

    OrderDto assemblyOrder(String orderId);

    OrderDto assemblyOrderWithError(String orderId);
}
