package com.web.api;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.web.domain.Customer;
import com.web.repository.CustomersRepository;

@RestController
public class CustomerAPI {
    
    @Autowired CustomersRepository repo;

    @GetMapping("/customers")
    public Iterable<Customer> getAll() {
        return repo.findAll();
    }
    
    @GetMapping("/purchases/{id}")
    public Optional<Purchase> getPurchase(@PathVariable long id) {
        return repo.findById(id);
    }

    @GetMapping("/")
    public String health() {
        return "Resource Server is running.";
    }
}

