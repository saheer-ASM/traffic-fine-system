package com.slpolice.trafficfine.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "traffic_fines", indexes = {
    @Index(name = "idx_reference", columnList = "reference"),
    @Index(name = "idx_driver", columnList = "driver_id"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_district", columnList = "district")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrafficFine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String reference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private User driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "officer_id", nullable = false)
    private User officer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private FineCategory category;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private FineStatus status = FineStatus.PENDING;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String vehicleRegistration;

    private String notes;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime issuedAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public enum FineStatus {
        PENDING, PAID, CANCELLED
    }
}
