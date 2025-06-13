package ru.yandex.practicum.dto.store;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private UUID productId;
    private String productName;
    private String description;
    private String imageSrc;
    private String quantityState;
    private String productState;
    private String productCategory;
    private BigDecimal price;
}