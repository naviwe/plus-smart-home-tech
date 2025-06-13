package ru.yandex.practicum.dto.store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeProductQuantityRequest {

    private UUID productId;
    private long newQuantity;
}