package com.slpolice.trafficfine.service;

import com.slpolice.trafficfine.dto.PaymentRequest;
import com.slpolice.trafficfine.dto.PaymentDto;
import com.slpolice.trafficfine.entity.Payment;
import com.slpolice.trafficfine.entity.TrafficFine;
import com.slpolice.trafficfine.entity.User;
import com.slpolice.trafficfine.exception.ResourceNotFoundException;
import com.slpolice.trafficfine.exception.UnauthorizedException;
import com.slpolice.trafficfine.repository.PaymentRepository;
import com.slpolice.trafficfine.repository.TrafficFineRepository;
import com.slpolice.trafficfine.repository.UserRepository;
import com.slpolice.trafficfine.mapper.PaymentMapper;
import com.slpolice.trafficfine.sms.SmsService;
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
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final TrafficFineRepository fineRepository;
    private final UserRepository userRepository;
    private final PaymentMapper paymentMapper;
    private final SmsService smsService;

    @Transactional
    public PaymentDto processPayment(PaymentRequest request) {
        String payerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User payer = userRepository.findByEmail(payerEmail)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        TrafficFine fine = fineRepository.findById(request.getFineId())
            .orElseThrow(() -> new ResourceNotFoundException("Fine not found"));

        if (!fine.getDriver().getId().equals(payer.getId())) {
            throw new UnauthorizedException("You can only pay your own fines");
        }

        if (!fine.getStatus().equals(TrafficFine.FineStatus.PENDING)) {
            throw new UnauthorizedException("Fine is not pending");
        }

        String transactionId = ReferenceGenerator.generatePaymentReference();
        
        Payment payment = Payment.builder()
            .transactionId(transactionId)
            .fine(fine)
            .payer(payer)
            .amount(fine.getAmount())
            .paymentMethod(Payment.PaymentMethod.valueOf(request.getPaymentMethod()))
            .paymentGatewayReference(request.getPaymentGatewayReference())
            .notes(request.getNotes())
            .build();

        payment = paymentRepository.save(payment);
        
        fine.setStatus(TrafficFine.FineStatus.PAID);
        fineRepository.save(fine);

        log.info("Payment processed: {} for fine: {}", transactionId, fine.getReference());

        smsService.sendPaymentConfirmation(payer.getPhone(), transactionId, fine.getAmount().toString());
        smsService.sendOfficerNotification(fine.getOfficer().getPhone(), fine.getReference(), fine.getAmount().toString());

        return paymentMapper.toDto(payment);
    }

    public Page<PaymentDto> getPaymentHistory(Long userId, Pageable pageable) {
        return paymentRepository.findByPayerId(userId, pageable)
            .map(paymentMapper::toDto);
    }
}
