package com.slpolice.trafficfine.repository;

import com.slpolice.trafficfine.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByTransactionId(String transactionId);
    Page<Payment> findByPayerId(Long payerId, Pageable pageable);
    
    @Query("SELECT SUM(p.amount) FROM Payment p")
    BigDecimal getTotalCollections();
    
    @Query("SELECT p FROM Payment p WHERE p.fine.district = :district")
    List<Payment> findByDistrict(@Param("district") String district);
}
