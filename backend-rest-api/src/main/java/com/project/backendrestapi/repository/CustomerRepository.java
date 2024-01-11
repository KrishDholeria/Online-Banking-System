package com.project.backendrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backendrestapi.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, String> {

    
}
