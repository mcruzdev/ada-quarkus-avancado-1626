package tech.ada.filter;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CustomResponseFilter implements ContainerResponseFilter {

    @Inject
    MeterRegistry meterRegistry;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        Log.info("Returning response with body " + responseContext.getEntity());
    }
}
