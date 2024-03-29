package com.project.backendrestapi.service;

import com.project.backendrestapi.model.Transaction;
import com.project.backendrestapi.model.Account;
import com.project.backendrestapi.model.Branch;
import com.project.backendrestapi.model.Customer;
import com.project.backendrestapi.repository.CustomerRepository;
import com.project.backendrestapi.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;

    public TransactionService(TransactionRepository transactionRepository, CustomerRepository customerRepository) {
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> getTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId);
    }

    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Optional<Transaction> updateTransaction(Long transactionId, Transaction updatedTransaction) {
        Optional<Transaction> existingTransaction = transactionRepository.findById(transactionId);

        if (existingTransaction.isPresent()) {
            Transaction existing = existingTransaction.get();
            existing.setTransactionId(transactionId);
            existing.setTransactionType(updatedTransaction.getTransactionType());
            existing.setAmount(updatedTransaction.getAmount());
            existing.setTransactionDate(updatedTransaction.getTransactionDate());

            // Handle relationships
            existing.setAccount(updatedTransaction.getAccount());
            existing.setRelatedTransaction(updatedTransaction.getRelatedTransaction());

            transactionRepository.save(existing);

            return Optional.of(existing);
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteTransaction(Long transactionId) {
        if (transactionRepository.existsById(transactionId)) {
            transactionRepository.deleteById(transactionId);
            return true;
        } else {
            return false;
        }
    }

    // Additional methods for handling relationships if needed

    // Example method for creating a related transaction
    public Transaction createRelatedTransaction(Transaction mainTransaction, Transaction relatedTransaction) {
        mainTransaction.setRelatedTransaction(relatedTransaction);
        transactionRepository.save(mainTransaction);
        return mainTransaction;
    }

    // Example method for associating a transaction with an account
    public void associateTransactionWithAccount(Long transactionId, Account account) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(transactionId);
        if (transactionOptional.isPresent()) {
            Transaction transaction = transactionOptional.get();
            transaction.setAccount(account);
            transactionRepository.save(transaction);
        }
    }

    // Example method for associating a transaction with a branch
    // public void associateTransactionWithBranch(Long transactionId, Branch branch)
    // {
    // Optional<Transaction> transactionOptional =
    // transactionRepository.findById(transactionId);
    // if (transactionOptional.isPresent()) {
    // Transaction transaction = transactionOptional.get();
    // transaction.setBranch(branch);
    // transactionRepository.save(transaction);
    // }
    // }

    public List<Transaction> getTransactionsByCustomerId(Long customerId) {
        // Fetch transactions associated with the customer ID
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Get the account associated with the customer
        Account account = customer.getAccount();

        // Now, fetch transactions associated with this account
        return transactionRepository.findByAccount(account);
    }

    public List<Transaction> getTransactionsByDuration(String duration) {
        // Determine the start and end dates based on the provided duration
        Calendar calendar = Calendar.getInstance();
        Date endDate = calendar.getTime();
        Date startDate = null;

        switch (duration) {
            case "last6months":
                calendar.add(Calendar.MONTH, -6);
                startDate = calendar.getTime();
                break;
            case "lastmonth":
                calendar.add(Calendar.MONTH, -1);
                startDate = calendar.getTime();
                break;
            case "lastweek":
                calendar.add(Calendar.DAY_OF_MONTH, -7);
                startDate = calendar.getTime();
                break;
            default:
                // Handle unsupported duration
                throw new IllegalArgumentException("Unsupported duration: " + duration);
        }

        List<Transaction> transactions = transactionRepository.getTransactionsBetweenDates(startDate, endDate);

        return transactions;
    }

}
