package com.slpolice.trafficfine.controller;

import com.slpolice.trafficfine.dto.ApiResponse;
import com.slpolice.trafficfine.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class AdminController {
    private final AnalyticsService analyticsService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> getDashboardStats() {
        return ResponseEntity.ok(ApiResponse.success(analyticsService.getDashboardStats(), "Dashboard stats retrieved successfully"));
    }

    @GetMapping("/collections/district")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> getDistrictCollections() {
        return ResponseEntity.ok(ApiResponse.success(analyticsService.getDistrictCollections(), "District collections retrieved successfully"));
    }

    @GetMapping("/collections/category")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> getCategoryCollections() {
        return ResponseEntity.ok(ApiResponse.success(analyticsService.getCategoryCollections(), "Category collections retrieved successfully"));
    }
}
