package com.example.config;

import com.example.resource.CategoryResource;
import com.example.resource.ItemResource;
import com.example.resource.MetricsResource;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.internal.inject.AbstractBinder;

/**
 * Configuration class for the Jersey JAX-RS container.
 * <p>
 * This class extends {@link ResourceConfig} to register REST resources,
 * configure JSON serialization with Jackson, and integrate Prometheus
 * monitoring for performance metrics.
 * </p>
 * 
 * @author Halmaoui Abdellah
 * @version 1.0
 * @since 2025
 */
public class JerseyConfig extends ResourceConfig {

    /**
     * Singleton instance of PrometheusMeterRegistry for application-wide
     * monitoring.
     */
    private static PrometheusMeterRegistry prometheusRegistry;

    /**
     * Initializes the Jersey configuration, registers resources, and sets up
     * monitoring.
     */
    public JerseyConfig() {
        // Initialize Prometheus registry if not already done
        if (prometheusRegistry == null) {
            prometheusRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

            // Register JVM metrics
            new io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics().bindTo(prometheusRegistry);
            new io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics().bindTo(prometheusRegistry);
            new io.micrometer.core.instrument.binder.jvm.JvmGcMetrics().bindTo(prometheusRegistry);
            new io.micrometer.core.instrument.binder.system.ProcessorMetrics().bindTo(prometheusRegistry);
            new io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics().bindTo(prometheusRegistry);

            System.out.println("✅ Prometheus registry created with JVM metrics");
        }

        // Register HK2 binder to inject PrometheusMeterRegistry
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(prometheusRegistry).to(PrometheusMeterRegistry.class);
            }
        });

        // Register REST resource classes
        register(CategoryResource.class);
        register(ItemResource.class);
        register(MetricsResource.class); // Register as class, HK2 will inject the registry

        // Register Jackson for JSON
        register(org.glassfish.jersey.jackson.JacksonFeature.class);

        System.out.println("✅ Jersey configuration initialized with Prometheus metrics");
        System.out.println("   Registered resources: CategoryResource, ItemResource, MetricsResource");
    }

    /**
     * Provides access to the Prometheus registry.
     * 
     * @return the active PrometheusMeterRegistry
     */
    public static PrometheusMeterRegistry getPrometheusRegistry() {
        return prometheusRegistry;
    }
}
