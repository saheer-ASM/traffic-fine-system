package com.slpolice.trafficfine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DistrictCollection {
    private String district;
    private Long totalFines;
    private Long paidFines;
    private BigDecimal totalAmount;
    private BigDecimal collectedAmount;
}
