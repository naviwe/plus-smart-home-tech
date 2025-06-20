package ru.yandex.practicum.dto.shoppingcart;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeProductQuantityRequest {
    @NotNull
    private UUID productId;
    @NotNull
    private Long newQuantity;
}
