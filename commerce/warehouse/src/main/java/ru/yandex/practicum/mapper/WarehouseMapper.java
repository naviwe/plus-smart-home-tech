package ru.yandex.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.model.Product;

@Component
public class WarehouseMapper {

    public Product toEntity(NewProductInWarehouseRequest dto) {
        return Product.builder()
                .productId(dto.getProductId())
                .fragile(dto.isFragile())
                .weight(dto.getWeight())
                .width(dto.getDimension().getWidth())
                .height(dto.getDimension().getHeight())
                .depth(dto.getDimension().getDepth())
                .quantity(0L)
                .build();
    }
}