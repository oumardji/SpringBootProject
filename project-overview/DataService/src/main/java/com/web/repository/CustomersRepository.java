package com.web.repository;

import org.springframework.data.repository.CrudRepository;

import com.web.domain.Customer;

public interface CustomersRepository extends CrudRepository<Customer, Long> {

}
