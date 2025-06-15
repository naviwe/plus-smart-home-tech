package ru.yandex.practicum.dto.shoppingstore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.enums.QuantityState;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SetProductCountState {
    @NotBlank(message = "Product ID cannot be blank")
    private String productId;

    @NotNull(message = "Quantity state cannot be null")
    private QuantityState quantityState;
}