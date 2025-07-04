package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.enums.PaymentStatus;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id", nullable = false)
    private String paymentId;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "total_payment")
    private double totalPayment;

    @Column(name = "delivery_total")
    private double deliveryTotal;

    @Column(name = "fee_total")
    private double feeTotal;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}