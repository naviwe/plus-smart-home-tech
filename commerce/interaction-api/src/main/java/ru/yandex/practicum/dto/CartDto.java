package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {
    @NotNull
    private String shoppingCartId;

    @NotNull
    private String username;

    private boolean cartState;

    private Map<String, Long> products;
}