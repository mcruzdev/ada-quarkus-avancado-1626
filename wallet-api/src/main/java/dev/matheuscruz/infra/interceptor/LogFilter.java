package dev.matheuscruz.infra.interceptor;

import dev.matheuscruz.domain.student.StudentService;
import dev.matheuscruz.presentation.api.StudentResource;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

import java.time.Duration;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class LogFilter implements ContainerRequestFilter {

    @Inject
    StudentService service;

    @Override
    public void filter(ContainerRequestContext requestContext) {

        Uni.createFrom().voidItem()
                .onItem().delayIt().by(Duration.ofSeconds(1))
                .invoke(() -> Log.info("Receiving HTTP request with method: " + requestContext.getMethod()))
                .subscribe().with(ignored -> {});

//        fire-and-forget ...
//        new Thread(() -> {
//            try {
//                Thread.sleep(1000);
//                Log.info("Receiving HTTP request with method: " + requestContext.getMethod());
//            } catch (InterruptedException e) {
//
//            }
//        }).start();

        Log.info("------- xpto");
    }
}
