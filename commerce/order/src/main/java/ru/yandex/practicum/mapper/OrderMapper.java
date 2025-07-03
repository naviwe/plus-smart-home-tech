package ru.yandex.practicum.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.dto.OrderDto;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    Order orderDtoToOrder(OrderDto orderDto);

    OrderDto orderToOrderDto(Order order);

    List<OrderDto> mapListOrders(List<Order> orders);
}
