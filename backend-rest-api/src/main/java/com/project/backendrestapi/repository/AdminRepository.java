package com.project.backendrestapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backendrestapi.model.Admin;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUserName(String userName);
}
