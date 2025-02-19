package com.knistr.loyaltycloud.systemtest.client.user;

public record RegistrationRequest(boolean acceptPrivacyPolicy,
                                  boolean acceptTermsOfUse,
                                  String externalMasterUnitNumber,
                                  Address address,
                                  String dateOfBirth,
                                  String firstName,
                                  String gender,
                                  String lastName,
                                  Login login,
                                  String mailAddress,
                                  boolean emailCorrespondenceAllowed,
                                  boolean letterCorrespondenceAllowed,
                                  String phoneNumber) {
}
