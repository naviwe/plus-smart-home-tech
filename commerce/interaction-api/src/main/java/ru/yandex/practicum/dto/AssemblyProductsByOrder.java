package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssemblyProductsByOrder {
    @NotBlank
    private Map<String, Long> products;

    @NotBlank
    private String orderId;
}