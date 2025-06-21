package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.shoppingcart.CartDto;
import ru.yandex.practicum.dto.warehouse.AddProductInWarehouse;
import ru.yandex.practicum.dto.warehouse.AddressWarehouseDto;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouse;
import ru.yandex.practicum.dto.warehouse.ReserveProductsDto;
import ru.yandex.practicum.exception.ConditionsNotMetException;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.mapper.WarehouseMapper;
import ru.yandex.practicum.model.WarehouseProduct;
import ru.yandex.practicum.repository.WarehouseRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;

    @Transactional
    @Override
    public void createProductInWarehouse(NewProductInWarehouse request) {
        Optional<WarehouseProduct> product = findProduct(request.getProductId());
        if (product.isPresent())
            throw new ConditionsNotMetException("Cannot add a product that already exists in the database");
        warehouseRepository.save(warehouseMapper.toWarehouse(request));
    }

    @Override
    public ReserveProductsDto checkCountProducts(CartDto cartDto) {
        Map<String, Long> products = cartDto.getProducts();
        List<WarehouseProduct> warehouseProducts = warehouseRepository.findAllById(products.keySet());

        warehouseProducts.forEach(warehouseProduct -> {
            if (warehouseProduct.getQuantity() < products.get(warehouseProduct.getProductId())) {
                throw new ConditionsNotMetException("Insufficient quantity of product in the warehouse");
            }
        });

        double deliveryWeight = warehouseProducts.stream()
                .map(WarehouseProduct::getWeight)
                .mapToDouble(Double::doubleValue)
                .sum();

        double deliveryVolume = warehouseProducts.stream()
                .map(product -> product.getDimension().getDepth()
                        * product.getDimension().getHeight()
                        * product.getDimension().getWidth())
                .mapToDouble(Double::doubleValue)
                .sum();

        boolean fragile = warehouseProducts.stream().anyMatch(WarehouseProduct::isFragile);

        return ReserveProductsDto.builder()
                .deliveryWeight(deliveryWeight)
                .deliveryVolume(deliveryVolume)
                .fragile(fragile)
                .build();
    }

    @Transactional
    @Override
    public void addProductInWarehouse(AddProductInWarehouse request) {
        Optional<WarehouseProduct> product = findProduct(request.getProductId());
        if (product.isEmpty())
            throw new NotFoundException("Product not found in the warehouse");
        WarehouseProduct pr = product.get();
        pr.setQuantity(pr.getQuantity() + request.getQuantity());
        warehouseRepository.save(pr);
    }

    @Override
    public AddressWarehouseDto getAddressWarehouse() {
        return AddressWarehouseDto.builder()
                .country("Azerbaijan")
                .city("Baku")
                .street("Kharabakh")
                .house("5")
                .flat("3")
                .build();
    }

    private Optional<WarehouseProduct> findProduct(String productId) {
        return warehouseRepository.findById(productId);
    }
}