package com.knistr.loyaltycloud.systemtest.client.identity;

public record CustomerIdentificationResponse(String externalIdentificationNumber, String externalCustomerId, String externalIdentificationTypeNumber, String status,
                                            String validFrom, String downloadUrl) {
}
