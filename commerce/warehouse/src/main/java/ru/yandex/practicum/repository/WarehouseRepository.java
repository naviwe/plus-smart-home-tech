package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.WarehouseProduct;

import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<WarehouseProduct, String> {
    Optional<WarehouseProduct> getByProductId(String productId);
}