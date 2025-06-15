package ru.yandex.practicum.dto.shoppingcart;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class RemoveProductsRequest {
    @NotNull
    private List<UUID> productIds;
}