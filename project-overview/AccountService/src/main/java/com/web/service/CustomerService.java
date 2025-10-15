package com.web.service;

import com.web.model.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class CustomerService {
    
    @Value("${dataservice.url:http://localhost:8080/api}")
    private String dataServiceUrl;
    
    private final RestTemplate restTemplate;
    
    public CustomerService() {
        this.restTemplate = new RestTemplate();
    }
    
    // Create a new customer in the DataService
    public Customer createCustomer(Customer customer) {
        try {
            String url = dataServiceUrl + "/customers";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Customer> request = new HttpEntity<>(customer, headers);
            
            ResponseEntity<Customer> response = restTemplate.exchange(
                url, 
                HttpMethod.POST, 
                request, 
                Customer.class
            );
            
            return response.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                throw new RuntimeException("Customer with email " + customer.getEmail() + " already exists");
            }
            throw new RuntimeException("Failed to create customer: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Failed to communicate with Data Service: " + e.getMessage());
        }
    }
    
    // Find customer by name for authentication
    public Customer findCustomerByName(String name) {
        try {
            String url = dataServiceUrl + "/customers/byname";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> request = new HttpEntity<>(name, headers);
            
            ResponseEntity<Customer> response = restTemplate.exchange(
                url, 
                HttpMethod.POST, 
                request, 
                Customer.class
            );
            
            return response.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return null; // Customer not found
            }
            throw new RuntimeException("Failed to find customer: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Failed to communicate with Data Service: " + e.getMessage());
        }
    }
    
    // Check if DataService is available
    public boolean isDataServiceAvailable() {
        try {
            String url = dataServiceUrl + "/";
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            return false;
        }
    }
}