package ru.yandex.practicum.dto.warehouse;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddProductInWarehouse {
    private String productId;

    @NotBlank
    @Min(value = 1, message = "The quantity cannot be less than 1.")
    private int quantity;
}