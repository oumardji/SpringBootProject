package com.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
    
    // GET /api/ - Health check endpoint
    @GetMapping("/")
    public String health() {
        return "Data Service is running.";
    }
}