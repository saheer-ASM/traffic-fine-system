package com.slpolice.trafficfine.util;

import java.util.UUID;

public class ReferenceGenerator {
    private static final String FINE_PREFIX = "FIN";
    private static final String PAYMENT_PREFIX = "PAY";

    public static String generateFineReference() {
        return FINE_PREFIX + System.currentTimeMillis() + randomSuffix();
    }

    public static String generatePaymentReference() {
        return PAYMENT_PREFIX + System.currentTimeMillis() + randomSuffix();
    }

    private static String randomSuffix() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
