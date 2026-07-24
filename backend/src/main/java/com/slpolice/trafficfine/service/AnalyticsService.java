package com.slpolice.trafficfine.service;

import com.slpolice.trafficfine.dto.DashboardStats;
import com.slpolice.trafficfine.dto.DistrictCollection;
import com.slpolice.trafficfine.dto.CategoryCollection;
import com.slpolice.trafficfine.repository.TrafficFineRepository;
import com.slpolice.trafficfine.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsService {
    private final TrafficFineRepository fineRepository;
    private final PaymentRepository paymentRepository;

    public DashboardStats getDashboardStats() {
        long totalFines = fineRepository.count();
        long totalPaidFines = fineRepository.countPaidFines();
        long totalPendingFines = fineRepository.countPendingFines();
        BigDecimal totalCollections = paymentRepository.getTotalCollections();
        
        BigDecimal averageFineAmount = BigDecimal.ZERO;
        if (totalPaidFines > 0 && totalCollections != null) {
            averageFineAmount = totalCollections.divide(BigDecimal.valueOf(totalPaidFines), 2, java.math.RoundingMode.HALF_UP);
        }

        return DashboardStats.builder()
            .totalFines(totalFines)
            .totalPaidFines(totalPaidFines)
            .totalPendingFines(totalPendingFines)
            .totalCollections(totalCollections != null ? totalCollections : BigDecimal.ZERO)
            .averageFineAmount(averageFineAmount)
            .build();
    }

    public List<DistrictCollection> getDistrictCollections() {
        return fineRepository.findAll().stream()
            .collect(Collectors.groupingBy(fine -> fine.getDistrict()))
            .entrySet().stream()
            .map(entry -> {
                String district = entry.getKey();
                var fines = entry.getValue();
                long totalFines = fines.size();
                long paidFines = fines.stream()
                    .filter(f -> "PAID".equals(f.getStatus().toString()))
                    .count();
                BigDecimal totalAmount = fines.stream()
                    .map(f -> f.getAmount())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal collectedAmount = fines.stream()
                    .filter(f -> "PAID".equals(f.getStatus().toString()))
                    .map(f -> f.getAmount())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                return DistrictCollection.builder()
                    .district(district)
                    .totalFines(totalFines)
                    .paidFines(paidFines)
                    .totalAmount(totalAmount)
                    .collectedAmount(collectedAmount)
                    .build();
            })
            .collect(Collectors.toList());
    }

    public List<CategoryCollection> getCategoryCollections() {
        return fineRepository.findAll().stream()
            .collect(Collectors.groupingBy(fine -> fine.getCategory().getCode()))
            .entrySet().stream()
            .map(entry -> {
                String code = entry.getKey();
                var fines = entry.getValue();
                long totalFines = fines.size();
                long paidFines = fines.stream()
                    .filter(f -> "PAID".equals(f.getStatus().toString()))
                    .count();
                BigDecimal totalAmount = fines.stream()
                    .map(f -> f.getAmount())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal collectedAmount = fines.stream()
                    .filter(f -> "PAID".equals(f.getStatus().toString()))
                    .map(f -> f.getAmount())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                String description = fines.get(0).getCategory().getDescription();

                return CategoryCollection.builder()
                    .categoryCode(code)
                    .categoryDescription(description)
                    .totalFines(totalFines)
                    .paidFines(paidFines)
                    .totalAmount(totalAmount)
                    .collectedAmount(collectedAmount)
                    .build();
            })
            .collect(Collectors.toList());
    }
}
