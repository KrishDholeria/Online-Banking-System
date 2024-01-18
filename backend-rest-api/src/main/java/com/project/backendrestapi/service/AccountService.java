package com.project.backendrestapi.service;

import com.project.backendrestapi.model.Account;
import com.project.backendrestapi.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountById(Long accountId) {
        return accountRepository.findById(accountId);
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Optional<Account> updateAccount(Long accountId, Account updatedAccount) {
        Optional<Account> existingAccount = accountRepository.findById(accountId);

        if (existingAccount.isPresent()) {
            Account existing = existingAccount.get();
            existing.setAccountId(accountId);
            existing.setAccountNumber(updatedAccount.getAccountNumber());
            existing.setAccountBalance(updatedAccount.getAccountBalance());
            existing.setDateOpened(updatedAccount.getDateOpened());
            existing.setDateClosed(updatedAccount.getDateClosed());

            accountRepository.save(existing);

            return Optional.of(existing);
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteAccount(Long accountId) {
        if (accountRepository.existsById(accountId)) {
            accountRepository.deleteById(accountId);
            return true;
        } else {
            return false;
        }
    }
}
