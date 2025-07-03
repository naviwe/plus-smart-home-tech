package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.shoppingstore.ProductDto;
import ru.yandex.practicum.mapper.PaymentMapper;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.repository.PaymentRepository;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.OrderClient;
import ru.yandex.practicum.ShoppingStoreClient;
import ru.yandex.practicum.enums.PaymentStatus;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderClient orderClient;
    private final ShoppingStoreClient storeClient;

    @Override
    public PaymentDto addPayment(OrderDto orderDto) {
        checkOrder(orderDto);
        Payment payment = Payment.builder()
                .orderId(orderDto.getOrderId())
                .totalPayment(orderDto.getTotalPrice())
                .deliveryTotal(orderDto.getDeliveryPrice())
                .feeTotal(orderDto.getTotalPrice() * 0.1)
                .status(PaymentStatus.PENDING)
                .build();

        return paymentMapper.paymentToPaymentDto(paymentRepository.save(payment));
    }

    @Override
    public double getTotalCost(OrderDto orderDto) {
        checkOrder(orderDto);

        return orderDto.getProductPrice() + (orderDto.getProductPrice() * 0.1) + orderDto.getDeliveryPrice();
    }

    @Override
    public void refundPayment(String paymentId) {
        Payment payment = getPayment(paymentId);
        orderClient.paymentOrder(payment.getOrderId());
        payment.setStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);
    }

    @Override
    public double getProductsCost(OrderDto orderDto) {
        Map<String, Long> products = orderDto.getProducts();
        if (products == null)
            throw new NotFoundException("No products found in the order");
        return products.entrySet().stream()
                .mapToDouble(product -> {
                    ProductDto productDto = storeClient.getProductInfo(product.getKey());

                    return productDto.getPrice() * product.getValue();
                })
                .sum();
    }

    @Override
    public void failedPayment(String paymentId) {
        Payment payment = getPayment(paymentId);
        orderClient.paymentOrderFailed(payment.getOrderId());
        payment.setStatus(PaymentStatus.FAILED);
        paymentRepository.save(payment);
    }

    private void checkOrder(OrderDto orderDto) {
        if (orderDto.getDeliveryPrice() == null)
            throw new NotFoundException("Delivery price is missing");
        if (orderDto.getTotalPrice() == null)
            throw new NotFoundException("Total order amount is missing");
        if (orderDto.getProductPrice() == null)
            throw new NotFoundException("Product amount is missing");
    }

    private Payment getPayment(String paymentId) {
        Optional<Payment> payment = paymentRepository.findById(paymentId);
        if (payment.isEmpty())
            throw new NotFoundException("Payment with id " + paymentId + " not found");

        return payment.get();
    }
}