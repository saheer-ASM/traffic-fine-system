package com.slpolice.trafficfine.mapper;

import com.slpolice.trafficfine.dto.FineDto;
import com.slpolice.trafficfine.entity.TrafficFine;
import org.springframework.stereotype.Component;

@Component
public class FineMapper {
    public FineDto toDto(TrafficFine fine) {
        return FineDto.builder()
            .id(fine.getId())
            .reference(fine.getReference())
            .driverId(fine.getDriver().getId())
            .driverName(fine.getDriver().getFullName())
            .driverPhone(fine.getDriver().getPhone())
            .officerId(fine.getOfficer().getId())
            .officerName(fine.getOfficer().getFullName())
            .categoryId(fine.getCategory().getId())
            .categoryCode(fine.getCategory().getCode())
            .categoryDescription(fine.getCategory().getDescription())
            .amount(fine.getAmount())
            .status(fine.getStatus().toString())
            .district(fine.getDistrict())
            .location(fine.getLocation())
            .vehicleRegistration(fine.getVehicleRegistration())
            .notes(fine.getNotes())
            .issuedAt(fine.getIssuedAt())
            .updatedAt(fine.getUpdatedAt())
            .build();
    }
}
