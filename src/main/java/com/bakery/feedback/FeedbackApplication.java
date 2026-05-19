package com.bakery.feedback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Part 6 – Customer Feedback Module
 * Bakery Order and Custom Cake Booking Platform
 *
 * Entry point for the Spring Boot application.
 * @SpringBootApplication enables auto-configuration,
 * component scanning, and configuration properties.
 */
@SpringBootApplication
public class FeedbackApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeedbackApplication.class, args);
    }
}
