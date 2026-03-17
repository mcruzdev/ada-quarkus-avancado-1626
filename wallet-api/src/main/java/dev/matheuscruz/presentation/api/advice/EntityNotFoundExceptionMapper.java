package dev.matheuscruz.presentation.api.advice;

import dev.matheuscruz.domain.exception.EntityNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

@Provider
public class EntityNotFoundExceptionMapper implements ExceptionMapper<EntityNotFoundException> {

    @Override
    public Response toResponse(EntityNotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(
                        new SimpleProblemDetail(
                                exception.getMessage(),
                                "This can happen when the entity does not exist in the system"
                        )
                )
                .build();
    }

    @ServerExceptionMapper
    public Response handleGenericException(Exception exception) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(
                        new SimpleProblemDetail(
                                exception.getMessage(),
                                "Internal Server Error, please contact us through email@quarkus.com"
                        )
                )
                .build();
    }
}
