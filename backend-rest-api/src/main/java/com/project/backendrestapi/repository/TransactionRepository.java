package com.project.backendrestapi.repository;

import java.util.List;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.backendrestapi.model.Account;
import com.project.backendrestapi.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccount(Account account);

    // List<Transaction> findByTransactionDateBetween(Date startDate, Date endDate);
}
