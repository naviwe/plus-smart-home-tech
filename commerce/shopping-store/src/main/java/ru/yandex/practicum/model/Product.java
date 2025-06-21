package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.enums.ProductState;
import ru.yandex.practicum.enums.QuantityState;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @Column(name = "product_id", nullable = false)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "image_src")
    private String imageSrc;

    @Column(name = "quantity_state", nullable = false)
    @Enumerated(EnumType.STRING)
    private QuantityState quantityState;

    @Column(name = "product_state", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductState productState;

    @Column(name = "rating", nullable = false)
    private int rating;

    @Column(name = "product_category")
    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;

    @Column(name = "price")
    private float price;
}
