package com.knistr.loyaltycloud.systemtest.client.user;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.knistr.loyaltycloud.systemtest.client.identity.CustomerIdentificationResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient
public interface RegistrationClient {

  @POST
  @Path("admin/register")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  CustomerIdentificationResponse createUser(RegistrationRequest request, @HeaderParam("Authorization") String auth);

}
