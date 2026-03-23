package tech.ada.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import tech.ada.dto.SignInRequest;
import tech.ada.security.JwtGenerator;

import java.util.Map;

@Path("/users")
public class UserResource {

    @Inject
    JwtGenerator jwtGenerator;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/jws")
    public Response jws(SignInRequest request) {
        if ("admin@course.com".equals(request.email()) && "admin".equals(request.password())) {
            String token = jwtGenerator.generateSignJwt();
            return Response.ok(Map.of("token", token)).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/jwe")
    public Response jwe(SignInRequest request) {
        if ("admin@course.com".equals(request.email()) && "admin".equals(request.password())) {
            String token = jwtGenerator.generateEncryptedJwt();
            return Response.ok(Map.of("token", token)).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
}
