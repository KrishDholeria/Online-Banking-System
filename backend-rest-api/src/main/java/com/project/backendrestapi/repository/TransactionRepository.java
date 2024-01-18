package com.project.backendrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backendrestapi.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    
}
