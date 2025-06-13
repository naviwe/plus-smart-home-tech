package ru.yandex.practicum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.dto.warehouse.AddressDto;

import java.security.SecureRandom;

@Configuration
public class AddressConfig {

    private static final String[] RAW_ADDRESSES = {"ADDRESS_1", "ADDRESS_2"};

    private static final String SELECTED = RAW_ADDRESSES[new SecureRandom().nextInt(RAW_ADDRESSES.length)];

    @Bean
    public AddressDto warehouseAddress() {
        return new AddressDto(SELECTED, SELECTED, SELECTED, SELECTED, SELECTED);
    }
}