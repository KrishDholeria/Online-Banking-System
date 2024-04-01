package com.project.backendrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backendrestapi.model.Customer;
import java.util.List;
import java.util.Optional;

import com.project.backendrestapi.model.Account;


import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    public Customer findByAccount(Account account);
    Optional<Customer> findByUserName(String userName);
    boolean existsByUserName(String userName);
}
