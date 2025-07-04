package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.shoppingcart.CartDto;
import ru.yandex.practicum.dto.warehouse.ReserveProductsDto;
import ru.yandex.practicum.mapper.OrderMapper;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.repository.OrderRepository;
import ru.yandex.practicum.DeliveryClient;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.PaymentClient;
import ru.yandex.practicum.ShoppingCartClient;
import ru.yandex.practicum.enums.OrderState;
import ru.yandex.practicum.WarehouseClient;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ShoppingCartClient cartClient;
    private final WarehouseClient warehouseClient;
    private final DeliveryClient deliveryClient;
    private final PaymentClient paymentClient;

    @Override
    public List<OrderDto> getUserOrders(String username) {
        CartDto shoppingCart = cartClient.getShoppingCart(username);
        return orderMapper.mapListOrders(orderRepository.findByShoppingCartId(shoppingCart.getShoppingCartId()));
    }

    @Override
    public OrderDto createNewOrder(NewOrder request) {
        Order order = Order.builder()
                .shoppingCartId(request.getShoppingCart().getShoppingCartId())
                .products(request.getShoppingCart().getProducts())
                .state(OrderState.NEW)
                .build();
        order = orderRepository.save(order);

        ReserveProductsDto reserveProductsDto = warehouseClient.reserveProducts(
                AssemblyProductsByOrder.builder()
                        .orderId(order.getOrderId())
                        .products(request.getShoppingCart().getProducts())
                        .build()
        );

        order.setDeliveryWeight(reserveProductsDto.getDeliveryWeight());
        order.setDeliveryVolume(reserveProductsDto.getDeliveryVolume());
        order.setFragile(reserveProductsDto.getFragile());

        DeliveryDto deliveryDto = DeliveryDto.builder()
                .orderId(order.getOrderId())
                .fromAddress(warehouseClient.getAddressWarehouse())
                .toAddress(request.getDeliveryAddress())
                .build();
        deliveryDto = deliveryClient.createNewDelivery(deliveryDto);
        order.setDeliveryId(deliveryDto.getDeliveryId());
        order.setProductPrice(paymentClient.getProductsCost(orderMapper.orderToOrderDto(order)));
        paymentClient.addPayment(orderMapper.orderToOrderDto(order));
        return orderMapper.orderToOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto returnOrder(ReturnProduct request) {
        Order order = getOrder(request.getOrderId());
        warehouseClient.returnProductsToWarehouse(request.getProducts());
        order.setState(OrderState.PRODUCT_RETURNED);
        return orderMapper.orderToOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto paymentOrder(String orderId) {
        Order order = getOrder(orderId);
        order.setState(OrderState.PAID);
        return orderMapper.orderToOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto paymentOrderWithError(String orderId) {
        Order order = getOrder(orderId);
        order.setState(OrderState.PAYMENT_FAILED);
        return orderMapper.orderToOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto deliveryOrder(String orderId) {
        Order order = getOrder(orderId);
        order.setState(OrderState.DELIVERED);
        return orderMapper.orderToOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto deliveryOrderWithError(String orderId) {
        Order order = getOrder(orderId);
        order.setState(OrderState.DELIVERY_FAILED);
        return orderMapper.orderToOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto completedOrder(String orderId) {
        Order order = getOrder(orderId);
        order.setState(OrderState.COMPLETED);
        return orderMapper.orderToOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto calculateTotalOrder(String orderId) {
        Order order = getOrder(orderId);
        order.setTotalPrice(paymentClient.getTotalCost(orderMapper.orderToOrderDto(order)));
        return orderMapper.orderToOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto calculateDeliveryOrder(String orderId) {
        Order order = getOrder(orderId);
        order.setDeliveryPrice(deliveryClient.getCostDelivery(orderMapper.orderToOrderDto(order)));
        return orderMapper.orderToOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto assemblyOrder(String orderId) {
        Order order = getOrder(orderId);
        order.setState(OrderState.ASSEMBLED);
        return orderMapper.orderToOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto assemblyOrderWithError(String orderId) {
        Order order = getOrder(orderId);
        order.setState(OrderState.ASSEMBLY_FAILED);
        return orderMapper.orderToOrderDto(orderRepository.save(order));
    }

    private Order getOrder(String orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isEmpty())
            throw new NotFoundException("Order with id " + orderId + " not found");
        return order.get();
    }
}
