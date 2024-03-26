package com.project.backendrestapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long customerId;
    private String userName;
    private String password;
    private String panNo;

    @OneToOne
    private Person person;

    @OneToOne
    private Account account;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Beneficiary> beneficiaries;

    public List<Beneficiary> addBeneficiary(Beneficiary beneficiary){
        if(beneficiaries == null){
            beneficiaries = new ArrayList<Beneficiary>();
        }
        beneficiaries.add(beneficiary);
        return beneficiaries;
    }

}
