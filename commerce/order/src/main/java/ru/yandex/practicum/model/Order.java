package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.enums.OrderState;

import java.util.Map;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "shopping_cart_id")
    private String shoppingCartId;

    @ElementCollection
    @CollectionTable(name = "order_products", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<String, Long> products;

    @Column(name = "payment_id")
    private String paymentId;

    @Column(name = "delivery_id")
    private String deliveryId;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private OrderState state;

    @Column(name = "delivery_weight")
    private double deliveryWeight;

    @Column(name = "delivery_volume")
    private double deliveryVolume;

    @Column(name = "fragile")
    private boolean fragile;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "delivery_price")
    private double deliveryPrice;

    @Column(name = "product_price")
    private double productPrice;
}
