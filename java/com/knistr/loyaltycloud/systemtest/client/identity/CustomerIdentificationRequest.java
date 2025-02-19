package com.knistr.loyaltycloud.systemtest.client.identity;

public record CustomerIdentificationRequest(String externalIdentificationNumber, String externalIdentificationTypeNumber, String status,
                                            String validFrom) {
}
