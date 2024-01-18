package com.project.backendrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backendrestapi.model.Beneficiary;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, String>{
    
}
