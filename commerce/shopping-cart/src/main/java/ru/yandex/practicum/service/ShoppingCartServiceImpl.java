package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.shoppingcart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.warehouse.ReserveProductsDto;
import ru.yandex.practicum.dto.shoppingcart.CartDto;
import ru.yandex.practicum.exception.ConditionsNotMetException;
import ru.yandex.practicum.WarehouseClient;
import ru.yandex.practicum.exception.NoProductsInShoppingCartException;
import ru.yandex.practicum.mapper.ShoppingCartMapper;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.repository.ShoppingCartRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository cartRepository;
    private final ShoppingCartMapper cartMapper;
    private final WarehouseClient warehouseClient;

    @Override
    public CartDto getShoppingCart(String username) {
        checkUserPresence(username);
        return cartMapper.toShoppingCartDto(getCart(username));
    }

    @Transactional
    @Override
    public CartDto addProductsToCart(String username, Map<String, Long> items) {
        checkUserPresence(username);
        ShoppingCart shoppingCart = getCart(username);
        if (shoppingCart == null) {
            shoppingCart = ShoppingCart.builder()
                    .username(username)
                    .products(items)
                    .cartState(true)
                    .build();
        } else {
            Map<String, Long> products = shoppingCart.getProducts();
            products.putAll(items);
        }
        return cartMapper.toShoppingCartDto(cartRepository.save(shoppingCart));
    }

    @Transactional
    @Override
    public void deleteUserCart(String username) {
        checkUserPresence(username);
        ShoppingCart shoppingCart = getCart(username);
        shoppingCart.setCartState(false);
        cartRepository.save(shoppingCart);
    }

    @Transactional
    @Override
    public CartDto removeProductsFromCart(String username, List<UUID> productIds) {
        checkUserPresence(username);
        ShoppingCart cart = getCart(username);

        if (cart == null || cart.getProducts().isEmpty()) {
            throw new NoProductsInShoppingCartException("The shopping cart is empty or not found");
        }

        List<String> productIdsAsStrings = productIds.stream()
                .map(UUID::toString)
                .collect(Collectors.toList());

        Map<String, Long> products = cart.getProducts();
        productIdsAsStrings.forEach(products::remove);

        ShoppingCart savedCart = cartRepository.save(cart);
        CartDto cartDto = cartMapper.toShoppingCartDto(savedCart);

        if (cartDto.getShoppingCartId() == null) {
            cartDto.setShoppingCartId(savedCart.getShoppingCartId());
        }

        return cartDto;
    }

    @Transactional
    @Override
    public CartDto changeCountProductInCart(String username, ChangeProductQuantityRequest request) {
        checkUserPresence(username);
        ShoppingCart cart = getCart(username);

        if (cart == null) {
            throw new ConditionsNotMetException("Shopping cart not found");
        }

        String productIdStr = request.getProductId().toString();
        if (!cart.getProducts().containsKey(productIdStr)) {
            throw new NoProductsInShoppingCartException("Product not found in the shopping cart");
        }

        cart.getProducts().put(productIdStr, request.getNewQuantity());
        ShoppingCart savedCart = cartRepository.save(cart);
        return cartMapper.toShoppingCartDto(savedCart);
    }

    @Override
    public ReserveProductsDto reserveProducts(String nameUser) {
        checkUserPresence(nameUser);
        ShoppingCart shoppingCart = getCart(nameUser);
        return warehouseClient.checkAvailableProducts(cartMapper.toShoppingCartDto(shoppingCart));
    }

    private void checkUserPresence(String username) {
        if (username == null || username.isEmpty()) {
            throw new ConditionsNotMetException("User is missing");
        }
    }

    private ShoppingCart getCart(String username) {
        return cartRepository.findByUsername(username);
    }
}