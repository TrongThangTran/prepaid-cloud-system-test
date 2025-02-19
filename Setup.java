package com.knistr.loyaltycloud.systemtest;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.eclipse.microprofile.rest.client.RestClientBuilder;

import com.knistr.loyaltycloud.systemtest.client.identity.CustomerIdentificationRequest;
import com.knistr.loyaltycloud.systemtest.client.identity.CustomerIdentificationResponse;
import com.knistr.loyaltycloud.systemtest.client.identity.IdentityManagementClient;
import com.knistr.loyaltycloud.systemtest.client.keycloak.KeycloakClient;
import com.knistr.loyaltycloud.systemtest.client.user.Address;
import com.knistr.loyaltycloud.systemtest.client.user.Login;
import com.knistr.loyaltycloud.systemtest.client.user.RegistrationClient;
import com.knistr.loyaltycloud.systemtest.client.user.RegistrationRequest;

public class Setup {
  private static final String DEVELOP = "develop";
  private static final String DEVELOP_MT = "develop-mt";
  private static final String PREPROD = "preprod";

  private static final String KEYCLOAK_URL_TEMPLATE = "https://auth.%s.loyaltyharbor.dev/auth/realms/%s/protocol/openid-connect/token";
  private static final String REGISTRATION_URL_TEMPLATE = "https://api.%s-%s.loyaltyharbor.dev/auth/v1";
  private static final String IDENTITY_URL_TEMPLATE = "https://api.%s-%s.loyaltyharbor.dev/identitymanagement/v1";

  public static String getKeycloakAdminToken(String environment, String tenant, String username, String password) {
    return getKeycloakTokenForClient("react-admin", environment, tenant, username, password);
  }

  public static String getKeycloakTouchpointToken(String environment, String tenant, String username, String password) {
    return getKeycloakTokenForClient("touchpoint", environment, tenant, username, password);
  }

  public static String getKeycloakLoyaltyToken(String environment, String tenant, String username, String password) {
    return getKeycloakTokenForClient("react-loyalty", environment, tenant, username, password);
  }

  private static String getKeycloakTokenForClient(String keycloakClientId, String environment, String tenant, String username,
      String password) {
    String keycloakUrl = buildUrl(KEYCLOAK_URL_TEMPLATE, environment, tenant);
    KeycloakClient keycloakClient = RestClientBuilder.newBuilder()
        .baseUri(URI.create(keycloakUrl))
        .build(KeycloakClient.class);

    return keycloakClient.getAccessToken("password", keycloakClientId, username, password).access_token();
  }

  public static CustomerIdentificationResponse createUser(String environment, String tenant, String token) {
    String registrationUrl = buildUrl(REGISTRATION_URL_TEMPLATE, environment, tenant);

    RegistrationClient registrationClient = RestClientBuilder.newBuilder()
        .baseUri(URI.create(registrationUrl))
        .build(RegistrationClient.class);

    Login login = new Login(UUID.randomUUID().toString(), "Systemtest.12345");
    String user = UUID.randomUUID().toString();
    Address address = new Address("Hamburg", "DE", "42", "Loyalty Street", "22529");
    RegistrationRequest registrationRequest = new RegistrationRequest(true, true, "EXT_OPU_1", address, "1980-01-05",
        "Systemtest",
        "MALE", "Systemtest", login, user + "@mail.com", false, true, "49 40 98653214");
    return registrationClient.createUser(registrationRequest, "Bearer " + token);
  }

  public static CustomerIdentificationResponse createCustomerIdentification(String environment, String tenant, String token,
      String customerId) {
    String identityUrl = buildUrl(IDENTITY_URL_TEMPLATE, environment, tenant);

    IdentityManagementClient identityManagementClient = RestClientBuilder.newBuilder()
        .baseUri(URI.create(identityUrl))
        .build(IdentityManagementClient.class);
    String externalIdentificationNumber = UUID.randomUUID().toString();
    CustomerIdentificationRequest identificationRequest = new CustomerIdentificationRequest(externalIdentificationNumber, "EXT_2", "ACTIVE",
        "2022-01-15");
    return identityManagementClient.createCustomerIdentification(identificationRequest, customerId, "Bearer " + token);
  }

  public static List<CustomerIdentificationResponse> verifyCustomerIdentification(String environment, String tenant, String token,
      String customerId) {
    String identityUrl = buildUrl(IDENTITY_URL_TEMPLATE, environment, tenant);

    IdentityManagementClient identityManagementClient = RestClientBuilder.newBuilder()
        .baseUri(URI.create(identityUrl))
        .build(IdentityManagementClient.class);
    return Collections.singletonList(identityManagementClient.getCustomerIdentifications(customerId, "Bearer " + token));
  }

  private static String buildUrl(String template, String environment, String tenant) {
    String env;
    if (template.contains("https://auth")) {
      env = switch (environment) {
        case DEVELOP -> DEVELOP_MT;
        case PREPROD -> "preprod";
        default -> throw new IllegalArgumentException("Invalid environment");
      };
      return String.format(template, env, tenant);
    } else if (template.contains("https://api")) {
      env = switch (environment) {
        case DEVELOP -> "develop";
        case PREPROD -> "preprod";
        default -> throw new IllegalArgumentException("Invalid environment");
      };
    } else {
      throw new IllegalArgumentException("Invalid URL template");
    }
    return String.format(template, tenant, env);
  }
}
