package ru.yandex.practicum.dto.warehouse;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SizeDto {

    @NotNull
    @Min(value = 1, message = "Width must not be less than 1")
    private double width;

    @NotNull
    @Min(value = 1, message = "Height must not be less than 1")
    private double height;

    @NotNull
    @Min(value = 1, message = "Depth must not be less than 1")
    private double depth;
}