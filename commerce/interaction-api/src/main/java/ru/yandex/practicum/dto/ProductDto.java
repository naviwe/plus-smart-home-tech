package ru.yandex.practicum.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.enums.ProductState;
import ru.yandex.practicum.enums.QuantityState;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private String productId;  // Убрать @NotBlank - генерируется автоматически

    @NotBlank(message = "Наименование товара не может быть пустым")
    private String productName;

    @NotBlank(message = "Описание товара не может быть пустым")
    private String description;

    private String imageSrc;

    @NotNull(message = "Статус количества не может быть null")
    private QuantityState quantityState;

    @NotNull(message = "Статус товара не может быть null")
    private ProductState productState;

    @Min(value = 1, message = "Рейтинг не может быть меньше 1")
    @Max(value = 5, message = "Рейтинг не может быть больше 5")
    private int rating;

    @NotNull(message = "Категория товара не может быть null")
    private ProductCategory productCategory;

    @Min(value = 1, message = "Стоимость не может быть меньше 1")
    private float price;
}
