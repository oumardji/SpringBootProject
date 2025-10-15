package com.web.config;

import com.web.entity.Customer;
import com.web.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        // Load initial data only if no customers exist
        if (customerRepository.count() == 0) {
            Customer customer1 = new Customer("John Doe", "john.doe@email.com", "password123");
            Customer customer2 = new Customer("Jane Smith", "jane.smith@email.com", "password456");
            Customer customer3 = new Customer("Mike Johnson", "mike.johnson@email.com", "password789");
            
            customerRepository.save(customer1);
            customerRepository.save(customer2);
            customerRepository.save(customer3);
            
            System.out.println("Initial customer data loaded successfully!");
        }
    }
}