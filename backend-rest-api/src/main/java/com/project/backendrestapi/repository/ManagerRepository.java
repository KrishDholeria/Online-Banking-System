package com.project.backendrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backendrestapi.model.Manager;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
    
}
