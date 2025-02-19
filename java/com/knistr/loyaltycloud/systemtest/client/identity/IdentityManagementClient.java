package com.knistr.loyaltycloud.systemtest.client.identity;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface IdentityManagementClient {

  @POST
  @Path("customers/{externalCustomerId}/identifications")
  CustomerIdentificationResponse createCustomerIdentification(CustomerIdentificationRequest request, @PathParam("externalCustomerId") String externalCustomerId,
      @HeaderParam("Authorization") String auth);

  @GET
  @Path("customers/{externalCustomerId}/identifications")
  CustomerIdentificationResponse getCustomerIdentifications(@PathParam("externalCustomerId") String externalCustomerId,
      @HeaderParam("Authorization") String auth);
}
