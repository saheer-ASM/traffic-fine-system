package com.slpolice.trafficfine.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueFineRequest {
    @NotNull(message = "Driver ID is required")
    private Long driverId;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Vehicle registration is required")
    private String vehicleRegistration;

    private String notes;
}
