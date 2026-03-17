package tech.ada.client;


import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.Map;

@RegisterRestClient(configKey = "wallet-api")
public interface WalletApi {

    @POST
    @Path("/transfers/bonifications")
    Response addBonification(
            AddBonusRequest request
    );

    @GET
    @Path("/students/{email}")
    Response findByEmail(@PathParam("email") String email);

    record AddBonusRequest(String email) {
    }
    record GetStudentResponse(Long id, String email) {}
}
