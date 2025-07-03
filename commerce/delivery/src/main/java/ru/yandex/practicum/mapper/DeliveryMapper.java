package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.dto.DeliveryDto;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DeliveryMapper {
    Delivery deliveryDtoToDelivery(DeliveryDto dto);

    DeliveryDto deliveryToDeliveryDto(Delivery delivery);

    List<DeliveryDto> mapListDeliveries(List<Delivery> deliveries);
}
