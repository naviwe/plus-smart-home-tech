package ru.yandex.practicum.dto.shoppingstore;


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
    private String productId;

    @NotBlank(message = "Product name cannot be empty")
    private String productName;

    @NotBlank(message = "Product description cannot be empty")
    private String description;

    private String imageSrc;

    @NotNull(message = "Quantity state cannot be null")
    private QuantityState quantityState;

    @NotNull(message = "Product state cannot be null")
    private ProductState productState;

    @Min(value = 1, message = "Rating cannot be less than 1")
    @Max(value = 5, message = "Rating cannot be greater than 5")
    private int rating;

    @NotNull(message = "Product category cannot be null")
    private ProductCategory productCategory;

    @Min(value = 1, message = "Price cannot be less than 1")
    private float price;
}