package com.project.backendrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backendrestapi.model.Branch;

import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Long> {

    Optional<Branch> findByBranchCode(String branchCode);
    Optional<Branch> deleteBranchByBranchCode(String branchCode);
    Boolean existsByBranchCode(String branchCode);
}
