package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouse;
import ru.yandex.practicum.model.WarehouseProduct;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WarehouseMapper {
    @Mapping(target = "quantity", ignore = true)
    WarehouseProduct toWarehouse(NewProductInWarehouse request);
}