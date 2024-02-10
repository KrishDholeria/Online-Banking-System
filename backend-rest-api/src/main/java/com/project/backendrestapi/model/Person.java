
package com.project.backendrestapi.model;

import java.util.Date;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long personId;
    private String lastName;
    private String firstName;
    private Date dob;
    private String email;
    private String phoneNo;
    private String address;

    @OneToOne(mappedBy = "person")
    private Manager manager;

    @OneToOne(mappedBy = "person")
    private Customer customer;

    @OneToOne(mappedBy = "person")
    private Admin admin;

}
