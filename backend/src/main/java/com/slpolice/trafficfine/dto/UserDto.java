package com.slpolice.trafficfine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String email;
    private String fullName;
    private String phone;
    private String role;
    private String licenseNumber;
    private String vehicleRegistration;
    private String district;
    private Boolean active;
    private LocalDateTime createdAt;
}
