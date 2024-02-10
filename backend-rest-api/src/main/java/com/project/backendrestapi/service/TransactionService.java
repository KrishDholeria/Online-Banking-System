package com.project.backendrestapi.service;

import com.project.backendrestapi.model.Transaction;
import com.project.backendrestapi.model.Account;
import com.project.backendrestapi.model.Branch;
import com.project.backendrestapi.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
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
    // public void associateTransactionWithBranch(Long transactionId, Branch branch) {
    //     Optional<Transaction> transactionOptional = transactionRepository.findById(transactionId);
    //     if (transactionOptional.isPresent()) {
    //         Transaction transaction = transactionOptional.get();
    //         transaction.setBranch(branch);
    //         transactionRepository.save(transaction);
    //     }
    // }
}
