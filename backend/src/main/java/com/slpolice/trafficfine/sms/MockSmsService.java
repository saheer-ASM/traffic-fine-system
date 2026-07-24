package com.slpolice.trafficfine.sms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MockSmsService implements SmsService {

    @Override
    public void sendPaymentConfirmation(String phoneNumber, String transactionId, String amount) {
        log.info("[SMS] Payment Confirmation - Phone: {}, Transaction: {}, Amount: {}", 
                 phoneNumber, transactionId, amount);
        log.info("Message: Dear Driver, Your fine payment of Rs.{} (Ref: {}) has been confirmed. Thank you!", 
                 amount, transactionId);
    }

    @Override
    public void sendFineNotification(String phoneNumber, String fineReference, String amount) {
        log.info("[SMS] Fine Notification - Phone: {}, Fine Ref: {}, Amount: {}", 
                 phoneNumber, fineReference, amount);
        log.info("Message: You have been issued a traffic fine of Rs.{}. Reference: {}. Pay now via our portal.", 
                 amount, fineReference);
    }

    @Override
    public void sendOfficerNotification(String phoneNumber, String fineReference, String paidAmount) {
        log.info("[SMS] Officer Notification - Phone: {}, Fine Ref: {}, Paid Amount: {}", 
                 phoneNumber, fineReference, paidAmount);
        log.info("Message: Fine {} has been paid. Amount: Rs.{}. Thank you for your service.", 
                 fineReference, paidAmount);
    }
}
