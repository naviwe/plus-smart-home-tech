package ru.yandex.practicum.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.mapper.WarehouseMapper;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.repository.ProductRepository;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final ProductRepository productRepository;
    private final WarehouseMapper mapper;
    private final AddressDto currentAddress;

    @Override
    @Transactional
    public void registerNewProduct(NewProductInWarehouseRequest request) {
        if (productRepository.existsById(request.getProductId())) {
            throw new IllegalArgumentException("Product already registered in warehouse.");
        }
        Product product = mapper.toEntity(request);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void addProductQuantity(AddProductToWarehouseRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found in warehouse"));
        product.setQuantity(product.getQuantity() + request.getQuantity());
        productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public BookedProductsDto checkProductAvailability(ShoppingCartDto cart) {
        double totalWeight = 0;
        double totalVolume = 0;
        boolean hasFragile = false;

        for (Map.Entry<UUID, Long> entry : cart.getProducts().entrySet()) {
            UUID productId = entry.getKey();
            long requestedQuantity = entry.getValue();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

            if (product.getQuantity() < requestedQuantity) {
                throw new IllegalStateException("Not enough product: " + productId);
            }

            double volume = product.getWidth() * product.getHeight() * product.getDepth();
            totalVolume += volume * requestedQuantity;
            totalWeight += product.getWeight() * requestedQuantity;

            if (product.isFragile()) {
                hasFragile = true;
            }
        }

        return BookedProductsDto.builder()
                .deliveryVolume(totalVolume)
                .deliveryWeight(totalWeight)
                .fragile(hasFragile)
                .build();
    }

    @Override
    public AddressDto getWarehouseAddress() {
        return currentAddress;
    }
}