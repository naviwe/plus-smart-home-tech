package ru.yandex.practicum.dto.warehouse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookedProductsDto {

    private double deliveryWeight;
    private double deliveryVolume;
    private boolean fragile;
}