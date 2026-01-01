package com.example;

import com.example.config.HibernateUtil;
import com.example.config.JerseyConfig;
import com.example.service.DataGeneratorService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import com.example.resource.MetricsResource;

/**
 * Main application class for Variant A: Jersey (JAX-RS) + Hibernate.
 * <p>
 * This class bootstrap the application by initializing Hibernate, generating
 * test data,
 * and starting an embedded Jetty server on port 8080. It configures Jersey as
 * the
 * servlet container to handle RESTful requests.
 * </p>
 * 
 * @author Halmaoui Abdellah
 * @version 1.0
 * @since 2025
 */
public class Application {

    /**
     * Main entry point for the Variant A application.
     * <p>
     * Performs the following startup tasks:
     * <ul>
     * <li>Initializes SQL database connection via Hibernate</li>
     * <li>Populates the database with realistic test data</li>
     * <li>Starts an embedded Jetty server on port 8080</li>
     * <li>Configures Jersey with Prometheus monitoring integration</li>
     * </ul>
     * </p>
     * 
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("🚀 Starting Variant A - Jersey + Hibernate Application");
        System.out.println("============================================================");

        // Initialize Hibernate
        HibernateUtil.getSessionFactory();

        // Generate test data
        DataGeneratorService.generateData();

        // Create Jetty server
        Server server = new Server(8080);

        // Create servlet context
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        // Create Jersey servlet with JerseyConfig (which includes Prometheus)
        ServletHolder jerseyServlet = new ServletHolder(new ServletContainer(new JerseyConfig()));
        jerseyServlet.setInitOrder(0);
        context.addServlet(jerseyServlet, "/*");

        try {
            server.start();
            System.out.println("============================================================");
            System.out.println("✅ Server started successfully!");
            System.out.println("🌐 Server URL: http://localhost:8080");
            System.out.println("\n📊 Available Endpoints:");
            System.out.println("   GET    http://localhost:8080/items");
            System.out.println("   GET    http://localhost:8080/items/{id}");
            System.out.println("   GET    http://localhost:8080/items?categoryId={id}");
            System.out.println("   POST   http://localhost:8080/items");
            System.out.println("   PUT    http://localhost:8080/items/{id}");
            System.out.println("   DELETE http://localhost:8080/items/{id}");
            System.out.println();
            System.out.println("   GET    http://localhost:8080/categories");
            System.out.println("   GET    http://localhost:8080/categories/{id}");
            System.out.println("   POST   http://localhost:8080/categories");
            System.out.println("   PUT    http://localhost:8080/categories/{id}");
            System.out.println("   DELETE http://localhost:8080/categories/{id}");
            System.out.println("============================================================");

            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
            server.destroy();
        }
    }
}
