package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.SetProductCountState;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.enums.ProductState;
import ru.yandex.practicum.mapper.ProductMapper;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.repository.ShoppingStoreRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ShoppingStoreServiceImpl implements ShoppingStoreService {
    private final ShoppingStoreRepository storeRepository;
    private final ProductMapper productMapper;

    @Override
    public List<ProductDto> getProductsByCategory(ProductCategory category, ru.yandex.practicum.dto.Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(
                pageable.getPage(),
                pageable.getSize(),
                pageable.getSort() != null && !pageable.getSort().isEmpty()
                        ? Sort.by(pageable.getSort().toArray(new String[0]))
                        : Sort.unsorted()
        );
        Page<Product> productPage = storeRepository.findAllByProductCategory(category, pageRequest);
        log.info("Найдено {} продуктов для категории {}", productPage.getContent().size(), category);
        return productMapper.mapListProducts(productPage.getContent());
    }

    @Transactional
    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = productMapper.productDtoToProduct(productDto);
        Product savedProduct = storeRepository.save(product);
        log.info("Создан продукт: {}", savedProduct);
        return productMapper.productToProductDto(savedProduct);
    }

    @Transactional
    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        getProduct(productDto.getProductId());
        Product product = productMapper.productDtoToProduct(productDto);
        Product updatedProduct = storeRepository.save(product);
        log.info("Обновлён продукт: {}", updatedProduct);
        return productMapper.productToProductDto(updatedProduct);
    }

    @Transactional
    @Override
    public void removeProduct(String productId) {
        Product product = getProduct(productId);
        product.setProductState(ProductState.DEACTIVATE);
        storeRepository.save(product);
        log.info("Продукт с ID {} деактивирован", productId);
    }

    @Transactional
    @Override
    public void changeState(SetProductCountState request) {
        Product product = getProduct(request.getProductId());
        product.setQuantityState(request.getQuantityState());
        storeRepository.save(product);
        log.info("Изменено состояние количества для продукта {}: {}", request.getProductId(), request.getQuantityState());
    }

    @Override
    public List<ProductDto> getAllProducts(ru.yandex.practicum.dto.Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(
                pageable.getPage(),
                pageable.getSize(),
                pageable.getSort() != null && !pageable.getSort().isEmpty()
                        ? Sort.by(pageable.getSort().toArray(new String[0]))
                        : Sort.unsorted()
        );
        Page<Product> productPage = storeRepository.findAll(pageRequest);
        log.info("Найдено {} продуктов", productPage.getContent().size());
        return productMapper.mapListProducts(productPage.getContent());
    }

    @Override
    public ProductDto getInfoByProduct(String productId) {
        Product product = getProduct(productId);
        return productMapper.productToProductDto(product);
    }

    private Product getProduct(String idProduct) {
        Optional<Product> product = storeRepository.getByProductId(idProduct);
        if (product.isEmpty()) {
            throw new NotFoundException("Не найден продукт с id " + idProduct);
        }
        return product.get();
    }
}