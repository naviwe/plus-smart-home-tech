package ru.yandex.practicum.dto.shoppingstore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pageable {
    private int page;
    private int size;
    private List<String> sort;
}