package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.dto.PaymentDto;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {
    Payment paymentDtoToPayment(PaymentDto paymentDto);

    PaymentDto paymentToPaymentDto(Payment payment);

    List<PaymentDto> mapListPayments(List<Payment> payments);
}
