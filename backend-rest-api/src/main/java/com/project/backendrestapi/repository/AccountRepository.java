package com.project.backendrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.backendrestapi.model.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    public Optional<Account> findAccountByAccountNumber(String accountNo);
}
