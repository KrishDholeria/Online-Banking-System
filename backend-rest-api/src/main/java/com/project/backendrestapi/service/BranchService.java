package com.project.backendrestapi.service;

import com.project.backendrestapi.dto.BranchDto;
import com.project.backendrestapi.model.Branch;
import com.project.backendrestapi.model.Account;
//import com.project.backendrestapi.model.Manager;
import com.project.backendrestapi.model.Manager;
import com.project.backendrestapi.repository.BranchRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

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

    public Optional<Branch> getBranchByBranchCode(String branchCode){
        Optional<Branch> branch = branchRepository.findByBranchCode(branchCode);
        System.out.println("Branch:::::::::::::::" + branch.get());
        return Optional.of(branch.get());
    }

    public Branch createBranch(BranchDto branchDto) {
        StringBuilder bc = new StringBuilder("B4EV0");
        Random rand = new Random();
        for (int i = 0; i < 6; i++) {
            bc.append(rand.nextInt(10));
        }
        Branch branch = Branch.builder()
                .branchName(branchDto.getBranchName())
                .address(branchDto.getAddress())
                .phoneNumber(branchDto.getPhoneNumber())
                .branchCode(bc.toString())
                .build();
        branchRepository.save(branch);
        return branch;
    }

    public Optional<Branch> updateBranch(String branchCode, BranchDto updatedBranch) {
        Optional<Branch> existingBranch = branchRepository.findByBranchCode(branchCode);

        if (existingBranch.isPresent()) {
            Branch existing = existingBranch.get();
            existing.setBranchName(updatedBranch.getBranchName());
            existing.setAddress(updatedBranch.getAddress());
            existing.setPhoneNumber(updatedBranch.getPhoneNumber());

            branchRepository.save(existing);

            return Optional.of(existing);
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteBranch(String branchCode) {
        if (branchRepository.existsByBranchCode(branchCode)) {
            Branch branch = branchRepository.findByBranchCode(branchCode).get();
            branchRepository.deleteById(branch.getBranchId());
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

    public void assignManagerToBranch(Long branchId, Manager manager) {
        Optional<Branch> branchOptional = branchRepository.findById(branchId);
        if (branchOptional.isPresent()) {
            Branch branch = branchOptional.get();
            branch.getManagers().add(manager);
            branchRepository.save(branch);
        }
    }

    public BranchDto entityToDto(Branch branch){
        return BranchDto.builder()
                .branchCode(branch.getBranchCode())
                .branchName(branch.getBranchName())
                .address(branch.getAddress())
                .phoneNumber(branch.getPhoneNumber())
                .build();
    }
}

// }
