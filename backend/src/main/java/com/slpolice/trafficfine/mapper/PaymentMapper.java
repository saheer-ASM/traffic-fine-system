package com.slpolice.trafficfine.mapper;

import com.slpolice.trafficfine.dto.PaymentDto;
import com.slpolice.trafficfine.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {
    public PaymentDto toDto(Payment payment) {
        return PaymentDto.builder()
            .id(payment.getId())
            .transactionId(payment.getTransactionId())
            .fineId(payment.getFine().getId())
            .fineReference(payment.getFine().getReference())
            .payerId(payment.getPayer().getId())
            .payerName(payment.getPayer().getFullName())
            .amount(payment.getAmount())
            .paymentMethod(payment.getPaymentMethod().toString())
            .paymentGatewayReference(payment.getPaymentGatewayReference())
            .notes(payment.getNotes())
            .paidAt(payment.getPaidAt())
            .build();
    }
}
