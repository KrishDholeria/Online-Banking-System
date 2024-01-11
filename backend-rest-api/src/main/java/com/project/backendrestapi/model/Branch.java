package com.project.backendrestapi.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Branch")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class Branch {

    @Id
    String branchId;
    String branchName;
    String branchCode;
    String address;
    String phoneNumber;

    @OneToMany(mappedBy = "branch")
    private List<Manager> managers;

    @OneToMany(mappedBy = "branch")
    private List<Account> accounts;

    @OneToMany(mappedBy = "branch")
    private List<Transaction> transactions;

}
