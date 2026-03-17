package dev.matheuscruz.presentation.api;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/env")
public class EnvResource {

    @ConfigProperty(name = "app.env")
    String appEnv;

    @GET
    public Uni<String> env() {
        Log.info("EnvResource");
        return Uni.createFrom()
                .item("Executing app with: " + appEnv);
    }
}
