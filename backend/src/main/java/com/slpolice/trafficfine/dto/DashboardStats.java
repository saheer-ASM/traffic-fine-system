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
public class DashboardStats {
    private Long totalFines;
    private Long totalPaidFines;
    private Long totalPendingFines;
    private BigDecimal totalCollections;
    private BigDecimal averageFineAmount;
}
