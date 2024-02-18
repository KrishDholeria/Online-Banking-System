package com.project.backendrestapi.service;

import com.project.backendrestapi.dto.AccountDto;
import com.project.backendrestapi.dto.BeneficiaryDto;
import com.project.backendrestapi.model.Account;
import com.project.backendrestapi.model.Beneficiary;
import com.project.backendrestapi.model.Branch;
import com.project.backendrestapi.model.Customer;
import com.project.backendrestapi.repository.AccountRepository;
import com.project.backendrestapi.repository.BranchRepository;
import com.project.backendrestapi.repository.CustomerRepository;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountService {

    @Autowired
    private final AccountRepository accountRepository;
    @Autowired
    private final BranchService branchService;
    @Autowired
    private final CustomerRepository customerRepository;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountById(Long accountId) {
        return accountRepository.findById(accountId);
    }

    public Account getAccountByAccountNo(String accountNo){
        return accountRepository.findAccountByAccountNumber(accountNo);
    }

    private String generateAccountNo(){
        Random rand = new Random();
        int[] nums = new int[12];
        StringBuilder ac = new StringBuilder();
        nums[0] = rand.nextInt(1,9);
        for(int i=1; i<12; i++){
            nums[i] = rand.nextInt(10);
            ac.append(nums[i]);
        }
        return ac.toString();
    }
    public Account createAccount(AccountDto account) {
        Branch branch = branchService.getBranchById(account.getBranchId()).get();
        Account newAccount = Account.builder()
                .accountBalance(account.getAccountBalance())
                .branch(branch)
                .accountNumber(generateAccountNo())
                .dateOpened(new Date())
                .build();
        return accountRepository.save(newAccount);
    }

    public Optional<Account> updateAccount(Long accountId, AccountDto updatedAccount) {
        Optional<Account> existingAccount = accountRepository.findById(accountId);
        Optional<Branch> branch = branchService.getBranchById(updatedAccount.getBranchId());
        Branch branch1 = branch.get();
        if (existingAccount.isPresent()) {
            Account existing = existingAccount.get();
            existing.setAccountId(accountId);
            existing.setAccountBalance(updatedAccount.getAccountBalance());
            existing.setBranch(branch1);

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

    public static AccountDto fromEntity(Account account) {
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountBalance(account.getAccountBalance());
        accountDto.setCutomerId(account.getCustomer().getCustomerId()); // Assuming customerId is a Long in the Customer entity
        accountDto.setBranchId(account.getBranch().getBranchId()); // Assuming branchId is a Long in the Branch entity
        return accountDto;
    }


}
