package com.project.backendrestapi.model;

import org.hibernate.query.sqm.StrictJpaComplianceViolation;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
public class Customer {

    @Id
    String CustomerId;
    String UserName;
    String Password;
    @OneToOne
    private Person person;
    private String PanNo;
}
