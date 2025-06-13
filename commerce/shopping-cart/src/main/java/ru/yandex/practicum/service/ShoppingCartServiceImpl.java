package ru.yandex.practicum.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.client.WarehouseClient;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.store.ChangeProductQuantityRequest;
import ru.yandex.practicum.exception.NoProductsInShoppingCartException;
import ru.yandex.practicum.exception.NotAuthorizedUserException;
import ru.yandex.practicum.mapper.ShoppingCartMapper;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.model.ShoppingCartItem;
import ru.yandex.practicum.repository.ShoppingCartItemRepository;
import ru.yandex.practicum.repository.ShoppingCartRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository cartRepository;
    private final ShoppingCartItemRepository itemRepository;
    private final ShoppingCartMapper cartMapper;
    private final WarehouseClient warehouseClient;

    @Override
    @Transactional(readOnly = true)
    public ShoppingCartDto getCart(String username) {
        return cartMapper.toDto(cartRepository.findByUsernameAndActiveTrue(username)
                .orElseThrow(() -> new NotAuthorizedUserException(username + ": not found")));
    }

    @Override
    @Transactional
    public void deactivateCart(String username) {
        ShoppingCart cart = cartRepository.findByUsernameAndActiveTrue(username)
                .orElseThrow(() -> new IllegalStateException("No active cart to deactivate"));

        cart.setActive(false);
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public ShoppingCartDto addProducts(String username, Map<UUID, Long> productsToAdd) {
        if (Objects.isNull(username) || username.trim().isEmpty()) {
            throw new NotAuthorizedUserException("Missing username");
        }

        if (Objects.isNull(productsToAdd) || productsToAdd.isEmpty()) {
            throw new NoProductsInShoppingCartException("Cart list cannot be null or empty");
        }

        ShoppingCart cart = cartRepository.findByUsernameAndActiveTrue(username).orElse(null);

        if (Objects.isNull(cart)) {
            cart = ShoppingCart.builder()
                    .id(UUID.randomUUID())
                    .username(username)
                    .active(true)
                    .items(new HashSet<>())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
        }

        if (!cart.isActive()) {
            throw new IllegalStateException("Cart is deactivated");
        }

        warehouseClient.checkProductAvailability(cartMapper.toDto(cart));

        for (Map.Entry<UUID, Long> entry : productsToAdd.entrySet()) {
            UUID productId = entry.getKey();
            long quantity = entry.getValue();

            if (quantity <= 0) {
                throw new IllegalStateException("Invalid quantity");
            }

            ShoppingCartItem item = itemRepository.findByCartIdAndProductId(cart.getId(), productId)
                    .orElse(ShoppingCartItem.builder()
                            .cart(cart)
                            .productId(productId)
                            .quantity(0)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build());

            item.setQuantity(item.getQuantity() + quantity);
            cart.getItems().add(item);
        }

        return cartMapper.toDto(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public ShoppingCartDto removeProducts(String username, List<UUID> productIds) {
        ShoppingCart cart = cartRepository.findByUsernameAndActiveTrue(username)
                .orElseThrow(() -> new IllegalStateException("No active cart found"));

        for (UUID productId : productIds) {
            itemRepository.findByCartIdAndProductId(cart.getId(), productId)
                    .ifPresent(item -> { cart.getItems().remove(item); });
        }

        return cartMapper.toDto(cartRepository.findById(cart.getId()).orElseThrow());
    }

    @Override
    @Transactional
    public ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest request) {
        ShoppingCart cart = cartRepository.findByUsernameAndActiveTrue(username)
                .orElseThrow(() -> new IllegalStateException("No active cart found"));

        ShoppingCartItem item = itemRepository.findByCartIdAndProductId(cart.getId(), request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not in cart"));

        item.setQuantity(request.getNewQuantity());
        itemRepository.save(item);

        return cartMapper.toDto(cartRepository.findById(cart.getId()).orElseThrow());
    }
}