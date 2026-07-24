package com.slpolice.trafficfine.controller;

import com.slpolice.trafficfine.dto.PaymentRequest;
import com.slpolice.trafficfine.dto.ApiResponse;
import com.slpolice.trafficfine.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<ApiResponse<?>> processPayment(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(paymentService.processPayment(request), "Payment processed successfully"));
    }

    @GetMapping("/history/{userId}")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<ApiResponse<?>> getPaymentHistory(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.success(paymentService.getPaymentHistory(userId, pageable), "Payment history retrieved successfully"));
    }
}
