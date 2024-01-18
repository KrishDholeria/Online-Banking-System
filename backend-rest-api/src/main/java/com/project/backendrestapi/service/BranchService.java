package com.project.backendrestapi.service;

import com.project.backendrestapi.model.Branch;
import com.project.backendrestapi.model.Account;
//import com.project.backendrestapi.model.Manager;
import com.project.backendrestapi.repository.BranchRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BranchService {

    @Autowired
    private final BranchRepository branchRepository;

    public BranchService(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    public Optional<Branch> getBranchById(Long branchId) {
        return branchRepository.findById(branchId);
    }

    public Branch createBranch(Branch branch) {
        return branchRepository.save(branch);
    }

    public Optional<Branch> updateBranch(Long branchId, Branch updatedBranch) {
        Optional<Branch> existingBranch = branchRepository.findById(branchId);

        if (existingBranch.isPresent()) {
            Branch existing = existingBranch.get();
            existing.setBranchId(branchId);
            existing.setBranchName(updatedBranch.getBranchName());
            existing.setBranchCode(updatedBranch.getBranchCode());
            existing.setAddress(updatedBranch.getAddress());
            existing.setPhoneNumber(updatedBranch.getPhoneNumber());

            existing.setManagers(updatedBranch.getManagers());
            existing.setAccounts(updatedBranch.getAccounts());
            existing.setTransactions(updatedBranch.getTransactions());

            branchRepository.save(existing);

            return Optional.of(existing);
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteBranch(Long branchId) {
        if (branchRepository.existsById(branchId)) {
            branchRepository.deleteById(branchId);
            return true;
        } else {
            return false;
        }
    }

    public void addAccountToBranch(Long branchId, Account account) {
        Optional<Branch> branchOptional = branchRepository.findById(branchId);
        if (branchOptional.isPresent()) {
            Branch branch = branchOptional.get();
            branch.getAccounts().add(account);
            branchRepository.save(branch);
        }
    }
}

// public void assignManagerToBranch(String branchId, Manager manager) {
// Optional<Branch> branchOptional = branchRepository.findById(branchId);
// if (branchOptional.isPresent()) {
// Branch branch = branchOptional.get();
// branch.setManager(manager);
// branchRepository.save(branch);
// }
// }
// }
