package org.acme.ai;

import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/uai")
public class UaiResource {

    @Inject
    BankUaiService bankUaiService;

    @POST
    public Response uai(Message request) {
        Log.info("Receiving message: " + request);
        String response = bankUaiService.ask(request.message());
        return Response.ok(response).build();
    }

    public record Message(String message) {
    }

}
