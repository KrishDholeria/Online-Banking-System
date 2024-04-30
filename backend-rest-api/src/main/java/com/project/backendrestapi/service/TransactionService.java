package com.project.backendrestapi.service;

import com.project.backendrestapi.model.Transactions;
import com.project.backendrestapi.dto.TransactionResponse;
import com.project.backendrestapi.model.Account;
import com.project.backendrestapi.model.Customer;
import com.project.backendrestapi.repository.CustomerRepository;
import com.project.backendrestapi.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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

    public List<Transactions> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<Transactions> getTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId);
    }

    public Transactions createTransaction(Transactions transactions) {
        return transactionRepository.save(transactions);
    }

    public Optional<Transactions> updateTransaction(Long transactionId, Transactions updatedTransactions) {
        Optional<Transactions> existingTransaction = transactionRepository.findById(transactionId);

        if (existingTransaction.isPresent()) {
            Transactions existing = existingTransaction.get();
            existing.setTransactionId(transactionId);
            existing.setTransactionType(updatedTransactions.getTransactionType());
            existing.setAmount(updatedTransactions.getAmount());
            existing.setTransactionDate(updatedTransactions.getTransactionDate());

            // Handle relationships
            existing.setAccount(updatedTransactions.getAccount());
            existing.setRelatedTransactions(updatedTransactions.getRelatedTransactions());

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
    public Transactions createRelatedTransaction(Transactions mainTransactions, Transactions relatedTransactions) {
        mainTransactions.setRelatedTransactions(relatedTransactions);
        transactionRepository.save(mainTransactions);
        return mainTransactions;
    }

    // Example method for associating a transaction with an account
    public void associateTransactionWithAccount(Long transactionId, Account account) {
        Optional<Transactions> transactionOptional = transactionRepository.findById(transactionId);
        if (transactionOptional.isPresent()) {
            Transactions transactions = transactionOptional.get();
            transactions.setAccount(account);
            transactionRepository.save(transactions);
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

    public List<Transactions> getTransactionsByDuration(String username, String duration) {

        Customer customer = customerRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Account account = customer.getAccount();

        // Fetch all transactions
        List<Transactions> allTransactions = transactionRepository.findByAccount(account);

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
            case "all":
                return allTransactions;
            default:
                throw new IllegalArgumentException("Invalid duration: " + duration);
        }
        Date startDate = cal.getTime();

        List<Transactions> filteredTransactions = allTransactions.stream()
                .filter(transaction -> transaction.getTransactionDate().after(startDate))
                .collect(Collectors.toList());

        return filteredTransactions;
    }

    public List<TransactionResponse> convertToResponse(List<Transactions> transactions) {
        List<TransactionResponse> transactionResponses = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("MMMM d, YYYY");
        SimpleDateFormat format2 = new SimpleDateFormat("hh:mm a");
        for (Transactions transaction : transactions) {
            TransactionResponse response = TransactionResponse.builder()
                    .responseCode("SUCCESS")
                    .responseMessage("Transaction retrieved successfully")
                    .refId(transaction.getReferenceId())
                    .amount(String.valueOf(transaction.getAmount()))
                    .type(transaction.getTransactionType())
                    .accountTo(transaction.getAccount().getAccountNumber())
                    .accountFrom(transaction.getRelatedTransactions().getAccount().getAccountNumber())
                    .date(format.format(transaction.getTransactionDate()))
                    .time(format2.format(transaction.getTransactionDate()))
                    .name(transaction.getAccount().getCustomer().getPerson().getFirstName() + " " + transaction.getAccount().getCustomer().getPerson().getLastName())
                    .build();
            transactionResponses.add(response);
        }
        return transactionResponses;
    }
}
