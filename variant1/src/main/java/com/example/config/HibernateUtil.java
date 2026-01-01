package com.example.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * Utility class for Hibernate configuration and SessionFactory management.
 * <p>
 * This class initializes Hibernate SessionFactory using programmatic
 * configuration.
 * It sets up the database connection to PostgreSQL, configures HikariCP
 * connection pool,
 * and registers annotated entity classes.
 * </p>
 * 
 * @author Halmaoui Abdellah
 * @version 1.0
 * @since 2025
 */
public class HibernateUtil {

    /**
     * Singleton instance of Hibernate SessionFactory.
     */
    private static SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();

            // Database connection settings (Docker PostgreSQL from docker-compose.yml)
            configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
            configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/benchmark_db");
            configuration.setProperty("hibernate.connection.username", "postgres");
            configuration.setProperty("hibernate.connection.password", "postgres");

            // Hibernate settings
            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            configuration.setProperty("hibernate.hbm2ddl.auto", "update");
            configuration.setProperty("hibernate.show_sql", "false");
            configuration.setProperty("hibernate.format_sql", "false");

            // Connection pool settings (HikariCP)
            configuration.setProperty("hibernate.connection.provider_class",
                    "org.hibernate.hikaricp.internal.HikariCPConnectionProvider");
            configuration.setProperty("hibernate.hikari.minimumIdle", "5");
            configuration.setProperty("hibernate.hikari.maximumPoolSize", "20");
            configuration.setProperty("hibernate.hikari.idleTimeout", "300000");

            // Performance settings
            configuration.setProperty("hibernate.jdbc.batch_size", "20");
            configuration.setProperty("hibernate.order_inserts", "true");
            configuration.setProperty("hibernate.order_updates", "true");

            // Register annotated classes
            configuration.addAnnotatedClass(com.example.model.Category.class);
            configuration.addAnnotatedClass(com.example.model.Item.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);

            System.out.println("✅ Hibernate SessionFactory initialized successfully");

        } catch (Throwable ex) {
            System.err.println("❌ Initial SessionFactory creation failed: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Provides access to the Hibernate SessionFactory.
     * 
     * @return the initialized SessionFactory singleton
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Shuts down Hibernate by closing the SessionFactory and all active
     * connections.
     * Should be called when the application is stopping.
     */
    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
