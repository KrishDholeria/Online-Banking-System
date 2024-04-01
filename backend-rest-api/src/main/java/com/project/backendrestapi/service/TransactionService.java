package com.project.backendrestapi.service;

import com.project.backendrestapi.model.Transaction;
import com.project.backendrestapi.dto.TransactionResponse;
import com.project.backendrestapi.model.Account;
import com.project.backendrestapi.model.Branch;
import com.project.backendrestapi.model.Customer;
import com.project.backendrestapi.repository.CustomerRepository;
import com.project.backendrestapi.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<Transaction> getTransactionsByDuration(String username, String duration) {

        Customer customer = customerRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Account account = customer.getAccount();

        // Fetch all transactions
        List<Transaction> allTransactions = transactionRepository.findByAccount(account);

        // Calculate start date based on the selected duration
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        switch (duration) {
            case "last6Months":
                cal.add(Calendar.MONTH, -6);
                break;
            case "lastMonth":
                cal.add(Calendar.MONTH, -1);
                break;
            case "lastWeek":
                cal.add(Calendar.WEEK_OF_YEAR, -1);
                break;
            default:
                throw new IllegalArgumentException("Invalid duration: " + duration);
        }
        Date startDate = cal.getTime();

        List<Transaction> filteredTransactions = allTransactions.stream()
                .filter(transaction -> transaction.getTransactionDate().after(startDate))
                .collect(Collectors.toList());

        return filteredTransactions;
    }

    public List<TransactionResponse> convertToResponse(List<Transaction> transactions) {
        List<TransactionResponse> transactionResponses = new ArrayList<>();
        for (Transaction transaction : transactions) {
            TransactionResponse response = TransactionResponse.builder()
                    .responseCode("SUCCESS")
                    .responseMessage("Transaction retrieved successfully")
                    .refId(transaction.getReferenceId())
                    .amount(String.valueOf(transaction.getAmount()))
                    .type(transaction.getTransactionType())
                    .accountTo(transaction.getAccount().getAccountNumber())
                    .build();
            transactionResponses.add(response);
        }
        return transactionResponses;
    }
}
