package com.project.backendrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backendrestapi.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, String> {

}
