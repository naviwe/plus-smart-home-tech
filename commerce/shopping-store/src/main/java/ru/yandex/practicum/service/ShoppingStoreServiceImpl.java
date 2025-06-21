package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.shoppingstore.ProductDto;
import ru.yandex.practicum.dto.shoppingstore.SetProductCountState;
import ru.yandex.practicum.exception.ConditionsNotMetException;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.enums.ProductState;
import ru.yandex.practicum.mapper.ProductMapper;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.repository.ShoppingStoreRepository;

import java.util.*;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ShoppingStoreServiceImpl implements ShoppingStoreService {
    private final ShoppingStoreRepository storeRepository;
    private final ProductMapper productMapper;

    @Override
    public Page<ProductDto> getProductsByCategory(ProductCategory category, ru.yandex.practicum.dto.shoppingstore.Pageable pageable) {
        try {
            String sortFields = pageable.getSort() == null || pageable.getSort().isEmpty()
                    ? "productName"
                    : String.join(",", pageable.getSort());

            Pageable pageRequest = PageRequest.of(
                    pageable.getPage(),
                    pageable.getSize(),
                    Sort.by(Sort.Direction.ASC, sortFields.split(",")));

            Page<Product> productPage = storeRepository.findAllByProductCategory(category, pageRequest);

            return productPage.map(productMapper::productToProductDto);

        } catch (Exception e) {
            log.error("Ошибка при получении продуктов по категории {}: {}", category, e.getMessage());
            return Page.empty();
        }
    }

    @Transactional
    @Override
    public ProductDto createProduct(ProductDto productDto) {
        if (storeRepository.getByProductId(productDto.getProductId()).isPresent())
            throw new ConditionsNotMetException("Создаваемый товар уже есть в базе данных");
        Product product = productMapper.productDtoToProduct(productDto);

        return productMapper.productToProductDto(storeRepository.save(product));
    }

    @Transactional
    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        getProduct(productDto.getProductId());

        return productMapper.productToProductDto(
                storeRepository.save(productMapper.productDtoToProduct(productDto)));
    }

    @Transactional
    @Override
    public boolean removeProduct(String productId) {
        Product product = getProduct(productId);

        if (product.getProductState() == ProductState.DEACTIVATE) {
            return true;
        }

        product.setProductState(ProductState.DEACTIVATE);
        Product savedProduct = storeRepository.save(product);

        log.info("Товар {} переведен в статус DEACTIVATE: {}",
                productId, savedProduct.getProductState());

        return savedProduct.getProductState() == ProductState.DEACTIVATE;
    }

    @Transactional
    @Override
    public boolean changeState(SetProductCountState request) {
        Product product = getProduct(request.getProductId());
        log.info("Установка quantityState {} для товара с ID {}", request.getQuantityState(), request.getProductId());
        product.setQuantityState(request.getQuantityState());
        Product savedProduct = storeRepository.save(product);
        log.info("Товар с ID {} сохранен с quantityState {}", request.getProductId(), savedProduct.getQuantityState());
        return true;
    }

    @Override
    public ProductDto getInfoByProduct(String productId) {
        return productMapper.productToProductDto(getProduct(productId));
    }

    private Product getProduct(String idProduct) {
        Optional<Product> product = storeRepository.getByProductId(idProduct);
        if (product.isEmpty())
            throw new NotFoundException("Не найден продукт с id " + idProduct);

        return product.get();
    }
}