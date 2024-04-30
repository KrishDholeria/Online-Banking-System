package com.project.backendrestapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.backendrestapi.model.Account;
import com.project.backendrestapi.model.Transactions;

public interface TransactionRepository extends JpaRepository<Transactions, Long> {
    List<Transactions> findByAccount(Account account);

    // List<Transaction> findByTransactionDateBetween(Date startDate, Date endDate);
}
