package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.SetProductCountState;
import ru.yandex.practicum.exception.ConditionsNotMetException;
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
        Sort sort = pageable.getSort().isEmpty()
                ? Sort.unsorted()
                : Sort.by(Sort.Direction.ASC, pageable.getSort().toArray(new String[0]));

        Pageable pageRequest = PageRequest.of(pageable.getPage(), pageable.getSize(), sort);
        List<Product> products = storeRepository.findAllByProductCategory(category, pageRequest);
        return productMapper.mapListProducts(products);
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
        product.setProductState(ProductState.DEACTIVATE);
        storeRepository.save(product);

        return true;
    }

    @Transactional
    @Override
    public boolean changeState(SetProductCountState request) {
        Product product = getProduct(request.getProductId());
        product.setQuantityState(request.getQuantityState());
        storeRepository.save(product);

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
