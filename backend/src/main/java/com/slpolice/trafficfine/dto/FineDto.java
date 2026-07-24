package com.slpolice.trafficfine.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FineDto {
    private Long id;
    private String reference;
    private Long driverId;
    private String driverName;
    private String driverPhone;
    private Long officerId;
    private String officerName;
    private Long categoryId;
    private String categoryCode;
    private String categoryDescription;
    private BigDecimal amount;
    private String status;
    private String district;
    private String location;
    private String vehicleRegistration;
    private String notes;
    private LocalDateTime issuedAt;
    private LocalDateTime updatedAt;
}
