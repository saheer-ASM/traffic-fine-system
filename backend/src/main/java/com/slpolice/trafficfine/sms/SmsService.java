package com.slpolice.trafficfine.sms;

public interface SmsService {
    void sendPaymentConfirmation(String phoneNumber, String transactionId, String amount);
    void sendFineNotification(String phoneNumber, String fineReference, String amount);
    void sendOfficerNotification(String phoneNumber, String fineReference, String paidAmount);
}
