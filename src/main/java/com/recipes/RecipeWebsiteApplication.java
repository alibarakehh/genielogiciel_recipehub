package com.recipes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main application class for Recipe Website
 * 
 * This is the entry point of the Spring Boot application.
 * It enables JPA auditing for automatic timestamp management.
 */
@SpringBootApplication
@EnableJpaAuditing
public class RecipeWebsiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecipeWebsiteApplication.class, args);
        System.out.println("\n==============================================");
        System.out.println("🍳 Recipe Website Application Started!");
        System.out.println("📖 API Documentation: http://localhost:8080/swagger-ui.html");
        System.out.println("🗄️  H2 Console: http://localhost:8080/h2-console");
        System.out.println("==============================================\n");
    }
}
