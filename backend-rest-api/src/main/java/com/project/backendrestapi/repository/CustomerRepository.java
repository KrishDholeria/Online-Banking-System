package com.project.backendrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backendrestapi.model.Customer;
import java.util.List;
import java.util.Optional;

import com.project.backendrestapi.model.Account;


public interface CustomerRepository extends JpaRepository<Customer, Long> {
    public Customer findByAccount(Account account);

}
