package com.project.backendrestapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Beneficiary {

    @Id
    String BeneficiaryId;
    String BeneficiaryName;

    @ManyToOne
    private Account account;


    
}
