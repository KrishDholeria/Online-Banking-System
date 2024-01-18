package com.project.backendrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.backendrestapi.model.Account;

public interface AccountRepository extends JpaRepository<Account, String> {

}
