package com.knistr.loyaltycloud.systemtest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knistr.loyaltycloud.systemtest.client.identity.CustomerIdentificationResponse;
import com.knistr.loyaltycloud.systemtest.client.user.Address;
import com.knistr.loyaltycloud.systemtest.client.user.Login;
import com.knistr.loyaltycloud.systemtest.client.user.RegistrationRequest;

public class CustomerIdentificationTest {

  private static final Logger LOG = LoggerFactory.getLogger(CustomerIdentificationTest.class);

  @Test
  public void createCustomerIdentification() throws Exception {
    // GIVEN
    String environment = System.getProperty("environment");
    String tenant = System.getProperty("tenant");
    String username = System.getProperty("username");
    String password = System.getProperty("password");

    // WHEN
    String adminToken = getAdminToken(environment, tenant, username, password);
    String customerId = createCustomer(environment, tenant, adminToken);

    String touchpointToken = getTouchpointToken(environment, tenant, username, password);
    CustomerIdentificationResponse identificationResponse = createCustomerIdentification(environment, tenant, touchpointToken, customerId);

    // THEN
    verifyCustomerIdentification(environment, tenant, touchpointToken, customerId, identificationResponse);
  }

  private static String getAdminToken(String environment, String tenant, String username, String password) {
    String token = Setup.getKeycloakAdminToken(environment, tenant, username, password);
    LOG.info("Received JWT react-admin: {}", token);
    return token;
  }

  private static String createCustomer(String environment, String tenant, String token) throws Exception {
    Address address = new Address("Hamburg", "DE", "42", "Loyalty Street", "22529");
    String username = UUID.randomUUID().toString();
    Login login = new Login(username, "Systemtest.12345");
    RegistrationRequest registrationRequest = new RegistrationRequest(true, true, "EXT_OPU_1", address, "1980-01-05",
        "SystemTest",
        "MALE", "SystemTest", login, username + "@mail.com", false, true, "49 40 98653214");

    ObjectMapper objectMapper = new ObjectMapper();
    String customerPayload = objectMapper.writeValueAsString(registrationRequest);
    LOG.info("To be created user: {}", customerPayload);

    try {
      CustomerIdentificationResponse customerResponse = Setup.createUser(environment, tenant, token);
      String customerId = customerResponse.externalCustomerId();
      LOG.info("CustomerId of created user: {}", customerId);
      return customerId;
    } catch (jakarta.ws.rs.WebApplicationException e) {
      LOG.error("Error response from server: " + e.getResponse().readEntity(String.class));
      throw e;
    }
  }

  private static String getTouchpointToken(String environment, String tenant, String username, String password) {
    String touchpointToken = Setup.getKeycloakTouchpointToken(environment, tenant, username, password);
    LOG.info("Received JWT touchpoint: {}", touchpointToken);
    return touchpointToken;
  }

  private static CustomerIdentificationResponse createCustomerIdentification(String environment, String tenant, String touchpointToken,
      String customerId) {
    CustomerIdentificationResponse identificationResponse = Setup.createCustomerIdentification(environment, tenant, touchpointToken,
        customerId);
    LOG.info("Created identification externalIdentificationNumber: {}", identificationResponse.externalIdentificationNumber());
    LOG.info("Created identification externalIdentificationTypeNumber: {}", identificationResponse.externalIdentificationTypeNumber());
    LOG.info("Created identification status: {}", identificationResponse.status());
    LOG.info("Created identification validFrom: {}", identificationResponse.validFrom());
    LOG.info("Created identification downloadUrl: {}", identificationResponse.downloadUrl());
    return identificationResponse;
  }

  private static void verifyCustomerIdentification(String environment, String tenant, String touchpointToken, String customerId,
      CustomerIdentificationResponse identificationResponse) {

    List<CustomerIdentificationResponse> customerIdentificationResponseGet = Setup.verifyCustomerIdentification(environment, tenant,
        touchpointToken, customerId);
    CustomerIdentificationResponse firstIdentification = customerIdentificationResponseGet.getFirst();

    LOG.info("Retrieved identification externalIdentificationNumber: {}", firstIdentification.externalIdentificationNumber());
    LOG.info("Retrieved identification externalIdentificationTypeNumber: {}", firstIdentification.externalIdentificationTypeNumber());
    LOG.info("Retrieved identification status: {}", firstIdentification.status());
    LOG.info("Retrieved identification validFrom: {}", firstIdentification.validFrom());

    assertThat(firstIdentification.externalIdentificationNumber(), equalTo(identificationResponse.externalIdentificationNumber()));
    assertThat(firstIdentification.externalIdentificationTypeNumber(), equalTo(identificationResponse.externalIdentificationTypeNumber()));
    assertThat(firstIdentification.status(), equalTo(identificationResponse.status()));
    assertThat(firstIdentification.validFrom(), equalTo(identificationResponse.validFrom()));
  }
}
