package com.slpolice.trafficfine.repository;

import com.slpolice.trafficfine.entity.TrafficFine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface TrafficFineRepository extends JpaRepository<TrafficFine, Long> {
    Optional<TrafficFine> findByReference(String reference);
    Page<TrafficFine> findByDriverId(Long driverId, Pageable pageable);
    Page<TrafficFine> findByOfficerId(Long officerId, Pageable pageable);
    List<TrafficFine> findByStatus(String status);
    
    @Query("SELECT f FROM TrafficFine f WHERE f.district = :district AND f.status = :status")
    List<TrafficFine> findByDistrictAndStatus(@Param("district") String district, @Param("status") String status);
    
    @Query("SELECT COUNT(f) FROM TrafficFine f WHERE f.status = 'PAID'")
    long countPaidFines();
    
    @Query("SELECT COUNT(f) FROM TrafficFine f WHERE f.status = 'PENDING'")
    long countPendingFines();
}
