package com.slpolice.trafficfine.sms;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NotifyLkSmsServiceTest {

    @Test
    void shouldSendSmsWhenEnabledAndCredentialsAreConfigured() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenReturn(ResponseEntity.ok("ok"));

        NotifyLkSmsService service = new NotifyLkSmsService(
                restTemplate,
                true,
                "https://example.test/send",
                "user-id",
                "api-key",
                "sender"
        );

        service.sendOfficerNotification("+94771234567", "FIN123", "5000");

        verify(restTemplate).postForEntity(anyString(), any(), eq(String.class));
    }
}
