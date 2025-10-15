package com.web.service;

import java.util.Optional;

import com.web.domain.Customer;

public interface CustomersService {
	public void savePurchase(Purchase purchase);
	public Iterable<Purchase> findAllPurchases();
	public Optional<Purchase> findPurchaseById(long id);
}
