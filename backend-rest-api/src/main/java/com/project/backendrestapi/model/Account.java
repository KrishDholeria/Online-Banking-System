package com.project.backendrestapi.model;

import java.util.ArrayList;
import java.util.Date;

import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long accountId;
    private String accountNumber;
    private Double accountBalance;
    private Date dateOpened;
    private Date dateClosed;

    @OneToOne
    Customer customer;

    @ManyToOne
    Branch branch;

    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "account")
    private List<Beneficiary> beneficiaries;

    public List<Beneficiary> addBeneficiary(Beneficiary beneficiary){
        if(beneficiaries == null){
            beneficiaries = new ArrayList<Beneficiary>();
        }
        beneficiaries.add(beneficiary);
    }
}
