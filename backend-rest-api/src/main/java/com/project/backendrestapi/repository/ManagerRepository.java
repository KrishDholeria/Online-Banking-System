package com.project.backendrestapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backendrestapi.model.Manager;

public interface ManagerRepository extends JpaRepository<Manager, Long> {

    Optional<Manager> getManagerByuserName(String userName);

}
