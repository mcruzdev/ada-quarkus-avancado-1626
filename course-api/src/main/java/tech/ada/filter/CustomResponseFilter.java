package tech.ada.filter;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

// Client HTTP -> Quarkus -> Request Filter -> Resource -> Response Filter
@Provider
public class CustomResponseFilter implements ContainerResponseFilter {

    @Inject
    MeterRegistry meterRegistry;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        int status = responseContext.getStatus();
        if (status >= 500) {
            Counter.builder("response.error.counter")
                    .tag("status", "internal_server_error")
                    .register(meterRegistry)
                    .increment();
        }
    }
}
