package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.ShoppingCartItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem, UUID> {

    Optional<ShoppingCartItem> findByCartIdAndProductId(UUID cartId, UUID productId);

    List<ShoppingCartItem> findByCartId(UUID cartId);
}