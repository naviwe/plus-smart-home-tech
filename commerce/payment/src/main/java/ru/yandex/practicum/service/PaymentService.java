package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;

public interface PaymentService {
    PaymentDto addPayment(OrderDto orderDto);

    double getTotalCost(OrderDto orderDto);

    void refundPayment(String paymentId);

    double getProductsCost(OrderDto orderDto);

    void failedPayment(String paymentId);
}
