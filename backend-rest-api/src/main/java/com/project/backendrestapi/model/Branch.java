package com.project.backendrestapi.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Branch")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long branchId;
    private String branchName;
    private String branchCode;
    private String address;
    private String phoneNumber;

    // private String managerUsername;

    @OneToMany(mappedBy = "branch")
    private List<Manager> managers;

    @OneToMany(mappedBy = "branch")
    private List<Account> accounts;

    // @OneToMany(mappedBy = "branch")
    // private List<Transaction> transactions;

}
