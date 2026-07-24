package com.slpolice.trafficfine.service;

import com.slpolice.trafficfine.dto.IssueFineRequest;
import com.slpolice.trafficfine.dto.FineDto;
import com.slpolice.trafficfine.entity.TrafficFine;
import com.slpolice.trafficfine.entity.User;
import com.slpolice.trafficfine.entity.FineCategory;
import com.slpolice.trafficfine.exception.ResourceNotFoundException;
import com.slpolice.trafficfine.repository.TrafficFineRepository;
import com.slpolice.trafficfine.repository.UserRepository;
import com.slpolice.trafficfine.repository.FineCategoryRepository;
import com.slpolice.trafficfine.mapper.FineMapper;
import com.slpolice.trafficfine.util.ReferenceGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FineService {
    private final TrafficFineRepository fineRepository;
    private final UserRepository userRepository;
    private final FineCategoryRepository categoryRepository;
    private final FineMapper fineMapper;

    @Transactional
    public FineDto issueFine(IssueFineRequest request) {
        String officerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User officer = userRepository.findByEmail(officerEmail)
            .orElseThrow(() -> new ResourceNotFoundException("Officer not found"));

        User driver = userRepository.findById(request.getDriverId())
            .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        FineCategory category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Fine category not found"));

        TrafficFine fine = TrafficFine.builder()
            .reference(ReferenceGenerator.generateFineReference())
            .driver(driver)
            .officer(officer)
            .category(category)
            .amount(category.getAmount())
            .status(TrafficFine.FineStatus.PENDING)
            .district(officer.getDistrict())
            .location(request.getLocation())
            .vehicleRegistration(request.getVehicleRegistration())
            .notes(request.getNotes())
            .build();

        fine = fineRepository.save(fine);
        log.info("Fine issued: {} for driver: {}", fine.getReference(), driver.getEmail());

        return fineMapper.toDto(fine);
    }

    public FineDto getFineByReference(String reference) {
        TrafficFine fine = fineRepository.findByReference(reference)
            .orElseThrow(() -> new ResourceNotFoundException("Fine not found"));
        return fineMapper.toDto(fine);
    }

    public Page<FineDto> getDriverFines(Long driverId, Pageable pageable) {
        return fineRepository.findByDriverId(driverId, pageable)
            .map(fineMapper::toDto);
    }

    public Page<FineDto> getOfficerFines(Long officerId, Pageable pageable) {
        return fineRepository.findByOfficerId(officerId, pageable)
            .map(fineMapper::toDto);
    }
}
