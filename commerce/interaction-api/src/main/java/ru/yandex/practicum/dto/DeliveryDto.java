package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.dto.warehouse.AddressWarehouseDto;
import ru.yandex.practicum.enums.DeliveryState;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryDto {
    @NotBlank
    private String deliveryId;

    @NotBlank
    private AddressWarehouseDto fromAddress;

    @NotBlank
    private AddressWarehouseDto toAddress;

    @NotBlank
    private String orderId;

    @NotBlank
    private DeliveryState deliveryState;
}