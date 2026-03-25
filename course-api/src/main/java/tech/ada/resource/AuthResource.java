package tech.ada.resource;

import io.quarkus.narayana.jta.QuarkusTransaction;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import tech.ada.dto.SignUpRequest;
import tech.ada.dto.SingInResponse;
import tech.ada.model.UserAuth;
import tech.ada.security.JwtGenerator;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;

@Path("/auth")
public class AuthResource {

    JwtGenerator jwtGenerator;

    @Inject // explicitly
    // @Inject implicitly
    public AuthResource(JwtGenerator jwtGenerator) {
        this.jwtGenerator = jwtGenerator;
    }

    @POST
    @Path("/sign-up")
    public Response signUp(SignUpRequest request) {
        QuarkusTransaction.requiringNew()
                .run(() -> UserAuth.add(
                        request.username(),
                        request.password(),
                        "USER"
                ));
        return Response.ok().build();
    }

    @POST
    @Path("/sign-in")
    public Response signIn(SignUpRequest request) {

        Optional<UserAuth> possibleUser = UserAuth.find("username", request.username())
                .firstResultOptional();

        if (possibleUser.isPresent()) {
            UserAuth userAuth = possibleUser.get();

            Duration expiresIn = Duration.ofHours(1);

            String token = jwtGenerator.generateJws(
                    userAuth.username, Set.of("USER", "ADMIN"), expiresIn
            );
            return Response.ok(new SingInResponse(
                    token, expiresIn.toMillis()
            )).build();
        }

        return Response.status(Response.Status.FORBIDDEN).build();
    }


//    @POST
//    @Path("/jws")
//    public Response generateJws() {
//        return Response.ok(token).build();
//        String token = jwtGenerator.generateJws(userAuth.username, userAuth.role, expiresIn);
//    }

    @POST
    @Path("/jwe")
    public Response generateJwe() {
        String token = jwtGenerator.generateJwe();
        return Response.ok(token).build();
    }
}
