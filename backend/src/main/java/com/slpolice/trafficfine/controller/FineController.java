package com.slpolice.trafficfine.controller;

import com.slpolice.trafficfine.dto.IssueFineRequest;
import com.slpolice.trafficfine.dto.ApiResponse;
import com.slpolice.trafficfine.service.FineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fines")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class FineController {
    private final FineService fineService;

    @PostMapping("/issue")
    @PreAuthorize("hasRole('OFFICER')")
    public ResponseEntity<ApiResponse<?>> issueFine(@Valid @RequestBody IssueFineRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(fineService.issueFine(request), "Fine issued successfully"));
    }

    @GetMapping("/reference/{reference}")
    public ResponseEntity<ApiResponse<?>> getFineByReference(@PathVariable String reference) {
        return ResponseEntity.ok(ApiResponse.success(fineService.getFineByReference(reference), "Fine retrieved successfully"));
    }

    @GetMapping("/driver/{driverId}")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<ApiResponse<?>> getDriverFines(
            @PathVariable Long driverId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.success(fineService.getDriverFines(driverId, pageable), "Fines retrieved successfully"));
    }

    @GetMapping("/officer/{officerId}")
    @PreAuthorize("hasRole('OFFICER')")
    public ResponseEntity<ApiResponse<?>> getOfficerFines(
            @PathVariable Long officerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.success(fineService.getOfficerFines(officerId, pageable), "Fines retrieved successfully"));
    }
}
