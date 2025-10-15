package com.web.controller;

import com.web.model.Customer;
import com.web.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.web.security.TokenService;

@RestController
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    TokenService tokenService;
    
    @Autowired
    CustomerService customerService;

    @PostMapping("/token")
    public String token(Authentication authentication) {
        logger.info("Token requested for user: " + authentication.getName());
        return tokenService.generateToken(authentication);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Customer customer) {
        try {
            logger.info("Registration requested for user: " + customer.getName());
            
            // Validate input
            if (customer.getName() == null || customer.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Name is required");
            }
            if (customer.getEmail() == null || customer.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Email is required");
            }
            if (customer.getPassword() == null || customer.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Password is required");
            }
            
            // Check if DataService is available
            if (!customerService.isDataServiceAvailable()) {
                logger.error("Data Service is not available");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Registration service is temporarily unavailable");
            }
            
            // Create customer in DataService
            Customer newCustomer = customerService.createCustomer(customer);
            logger.info("Customer created successfully: " + newCustomer.getName());
            
            return ResponseEntity.ok("Customer registered successfully");
            
        } catch (RuntimeException e) {
            logger.error("Registration failed: " + e.getMessage());
            if (e.getMessage().contains("already exists")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during registration", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Registration failed due to internal error");
        }
    }

    @GetMapping("/")
    public String health() {
        return "Authorization Server is running.";
    }
    
    @GetMapping("/status")
    public ResponseEntity<String> status() {
        try {
            boolean dataServiceAvailable = customerService.isDataServiceAvailable();
            if (dataServiceAvailable) {
                return ResponseEntity.ok("Account Service is running. Data Service connection: OK");
            } else {
                return ResponseEntity.ok("Account Service is running. Data Service connection: FAILED");
            }
        } catch (Exception e) {
            return ResponseEntity.ok("Account Service is running. Data Service connection: ERROR - " + e.getMessage());
        }
    }
}
