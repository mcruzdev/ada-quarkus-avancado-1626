package tech.ada.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import tech.ada.ai.UaiService;

import java.io.Serializable;

@Path("/uai")
public class UaiResource {


    @Inject
    UaiService uaiService;

    @POST
    public Response uai(Message request) {
        String response = uaiService.ask(request.message());
        return Response.ok(response).build();
    }


    public record Message(String message) {
    }

}
