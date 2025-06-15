package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.shoppingcart.CartDto;
import ru.yandex.practicum.dto.warehouse.AddProductInWarehouse;
import ru.yandex.practicum.dto.warehouse.AddressWarehouseDto;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouse;
import ru.yandex.practicum.dto.warehouse.ReserveProductsDto;

public interface WarehouseService {
    void createProductInWarehouse(NewProductInWarehouse request);

    ReserveProductsDto checkCountProducts(CartDto cartDto);

    void addProductInWarehouse(AddProductInWarehouse request);

    AddressWarehouseDto getAddressWarehouse();
}