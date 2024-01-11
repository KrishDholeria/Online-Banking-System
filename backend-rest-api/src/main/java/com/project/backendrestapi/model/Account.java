package com.project.backendrestapi.model;

import java.sql.Date;

import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Account {

    @Id
    String accountId;
    String accountNumber;
    Double accountBalance;
    Date dateOpened;
    Date dateClosed;

    @OneToOne
    Customer customer;

    @ManyToOne
    Branch branch;

    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "account")
    private List<Beneficiary> beneficiaries;
}
