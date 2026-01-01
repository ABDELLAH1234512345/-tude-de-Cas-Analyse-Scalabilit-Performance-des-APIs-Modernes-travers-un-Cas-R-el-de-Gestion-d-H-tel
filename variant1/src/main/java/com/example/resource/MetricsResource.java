package com.example.resource;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST Resource for exposing Prometheus metrics.
 * <p>
 * This endpoint provides real-time performance metrics (JVM, memory, CPU,
 * custom metrics)
 * in a format compatible with Prometheus scrapers.
 * </p>
 * 
 * @author Halmaoui Abdellah
 * @version 1.0
 * @since 2025
 */
@Path("/metrics")
public class MetricsResource {

    /**
     * Prometheus registry containing all application metrics.
     */
    private final PrometheusMeterRegistry registry;

    /**
     * Constructor used by HK2 for dependency injection.
     * 
     * @param registry the registry injected from JerseyConfig
     */
    @Inject
    public MetricsResource(PrometheusMeterRegistry registry) {
        this.registry = registry;
        System.out.println("✅ MetricsResource instantiated with registry: " + registry);
    }

    /**
     * Scrapes and returns all registered metrics in text/plain format.
     * 
     * @return 200 OK with Prometheus formatted metrics string
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getMetrics() {
        String metrics = registry.scrape();
        System.out.println("📊 Metrics endpoint called, returning " + metrics.length() + " bytes");
        return Response.ok(metrics).build();
    }
}
