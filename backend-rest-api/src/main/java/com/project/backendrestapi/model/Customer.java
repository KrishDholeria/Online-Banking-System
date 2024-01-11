package com.project.backendrestapi.model;

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
    private String PanNo;
   
    @OneToOne
    private Person person;

    @OneToOne(mappedBy = "customer")
    private Account account;

    
}
