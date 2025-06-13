package ru.yandex.practicum.dto.store;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SetProductQuantityStateRequest {
    private UUID productId;
    private String quantityState;
}