package ru.yandex.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.model.Product;
import java.util.Optional;

@Repository
public interface ShoppingStoreRepository extends JpaRepository<Product, String> {
    Page<Product> findAllByProductCategory(ProductCategory productCategory, Pageable pageable);

    Optional<Product> getByProductId(String productId);
}
