package ru.yandex.practicum.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.Pageable;
import ru.yandex.practicum.dto.store.ProductDto;
import ru.yandex.practicum.dto.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.mapper.ProductMapper;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.model.store.ProductCategory;
import ru.yandex.practicum.model.store.ProductState;
import ru.yandex.practicum.model.store.QuantityState;
import ru.yandex.practicum.repository.ProductRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public List<ProductDto> getProductsByCategory(ProductCategory category, Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPage(), pageable.getSize());
        return productRepository
                .findByProductCategoryAndProductState(category, ProductState.ACTIVE, pageRequest)
                .map(productMapper::toDto)
                .getContent();
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        product.setProductState(ProductState.ACTIVE);
        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        Product product = productRepository.findById(productDto.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + productDto.getProductId()));

        product.setProductName(productDto.getProductName());
        product.setDescription(productDto.getDescription());
        product.setImageSrc(productDto.getImageSrc());
        product.setPrice(productDto.getPrice());
        product.setQuantityState(QuantityState.valueOf(productDto.getQuantityState()));
        product.setProductCategory(ProductCategory.valueOf(productDto.getProductCategory()));
        product.setProductState(ProductState.valueOf(productDto.getProductState()));

        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    public boolean removeProduct(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + productId));
        product.setProductState(ProductState.DEACTIVATE);
        productRepository.save(product);
        return true;
    }

    @Override
    public boolean setProductQuantityState(SetProductQuantityStateRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + request.getProductId()));
        product.setQuantityState(QuantityState.valueOf(request.getQuantityState()));
        productRepository.save(product);
        return true;
    }

    @Override
    public ProductDto getProductById(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + productId));
        return productMapper.toDto(product);
    }
}