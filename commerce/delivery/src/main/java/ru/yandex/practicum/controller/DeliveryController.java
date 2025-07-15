package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.service.DeliveryService;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/delivery")
public class DeliveryController {
    private final DeliveryService deliveryService;

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public DeliveryDto createNewDelivery(@RequestBody DeliveryDto deliveryDto) {
        log.info("Creating new delivery request: {}", deliveryDto);
        return deliveryService.createNewDelivery(deliveryDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/successful")
    public void setDeliverySuccess(@RequestBody String deliveryId) {
        log.info("Marking delivery {} as successful", deliveryId);
        deliveryService.setDeliverySuccess(deliveryId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/picked")
    public void setDeliveryPicked(@RequestBody String deliveryId) {
        log.info("Marking delivery {} as picked up", deliveryId);
        deliveryService.setDeliveryPicked(deliveryId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/failed")
    public void setDeliveryFailed(@RequestBody String deliveryId) {
        log.info("Marking delivery {} as faile", deliveryId);
        deliveryService.setDeliveryFailed(deliveryId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/cost")
    public Double getCostDelivery(@RequestBody OrderDto orderDto) {
        log.info("Calculating delivery cost for order: {}", orderDto);
        return deliveryService.getCostDelivery(orderDto);
    }
}