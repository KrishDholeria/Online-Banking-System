
package com.project.backendrestapi.model;

import java.sql.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Person")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String PersonId;
    private String LastName;
    private String FirstName;
    private Date DOB;
    private int Age;
    private String Email;
    private String PhoneNo;
    private String Address;
    

    @OneToOne(mappedBy = "person")
    private Manager manager;

    @OneToOne(mappedBy = "person")
    private Customer customer;

    @OneToOne(mappedBy = "person")
    private Admin admin;
    
}
