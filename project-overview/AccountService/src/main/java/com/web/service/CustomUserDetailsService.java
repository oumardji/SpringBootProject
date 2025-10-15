package com.web.service;

import com.web.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private CustomerService customerService;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerService.findCustomerByName(username);
        
        if (customer == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        
        // Create a UserDetails object with the customer's credentials
        // In a real application, you would encode the password properly
        return User.builder()
                .username(customer.getName())
                .password("{noop}" + customer.getPassword()) // {noop} means no password encoding
                .authorities(Collections.emptyList()) // No specific authorities needed for this demo
                .build();
    }
}