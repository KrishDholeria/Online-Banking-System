package com.project.backendrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backendrestapi.model.Branch;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    
}
