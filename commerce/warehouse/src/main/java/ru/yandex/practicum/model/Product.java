package ru.yandex.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    private UUID productId;

    @Column(nullable = false)
    private boolean fragile;

    @Column(nullable = false)
    private double weight;

    @Column(nullable = false)
    private double width;

    @Column(nullable = false)
    private double height;

    @Column(nullable = false)
    private double depth;

    @Column(nullable = false)
    private long quantity;
}