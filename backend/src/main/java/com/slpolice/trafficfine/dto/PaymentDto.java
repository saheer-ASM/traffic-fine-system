package com.slpolice.trafficfine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDto {
    private Long id;
    private String transactionId;
    private Long fineId;
    private String fineReference;
    private Long payerId;
    private String payerName;
    private BigDecimal amount;
    private String paymentMethod;
    private String paymentGatewayReference;
    private String notes;
    private LocalDateTime paidAt;
}
