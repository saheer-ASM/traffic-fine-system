package com.slpolice.trafficfine.sms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@Primary
@Slf4j
public class NotifyLkSmsService implements SmsService {

    private final RestTemplate restTemplate;
    private final boolean enabled;
    private final String endpoint;
    private final String userId;
    private final String apiKey;
    private final String senderId;

    public NotifyLkSmsService(
            RestTemplate restTemplate,
            @Value("${notifylk.enabled:false}") boolean enabled,
            @Value("${notifylk.endpoint:}") String endpoint,
            @Value("${notifylk.user-id:}") String userId,
            @Value("${notifylk.api-key:}") String apiKey,
            @Value("${notifylk.sender-id:}") String senderId) {
        this.restTemplate = restTemplate;
        this.enabled = enabled;
        this.endpoint = endpoint;
        this.userId = userId;
        this.apiKey = apiKey;
        this.senderId = senderId;
    }

    @Override
    public void sendPaymentConfirmation(String phoneNumber, String transactionId, String amount) {
        sendSms(phoneNumber, "Dear Driver, your fine payment of Rs."
                + amount + " has been confirmed. Transaction: " + transactionId);
    }

    @Override
    public void sendFineNotification(String phoneNumber, String fineReference, String amount) {
        sendSms(phoneNumber, "You have been issued a traffic fine of Rs."
                + amount + ". Reference: " + fineReference);
    }

    @Override
    public void sendOfficerNotification(String phoneNumber, String fineReference, String paidAmount) {
        sendSms(phoneNumber, "Fine " + fineReference + " has been paid successfully. Amount: Rs."
                + paidAmount + ". Thank you for your service.");
    }

    private void sendSms(String phoneNumber, String message) {
        if (!enabled || endpoint.isBlank() || userId.isBlank() || apiKey.isBlank()) {
            log.info("Notify.lk SMS disabled or not configured. Message for {}: {}", phoneNumber, message);
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("user_id", userId);
        body.add("api_key", apiKey);
        body.add("sender_id", senderId);
        body.add("to", phoneNumber);
        body.add("message", message);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            restTemplate.postForEntity(endpoint, request, String.class);
            log.info("Notify.lk SMS sent to {}", phoneNumber);
        } catch (Exception ex) {
            log.error("Notify.lk SMS failed for {}: {}", phoneNumber, ex.getMessage());
        }
    }
}
