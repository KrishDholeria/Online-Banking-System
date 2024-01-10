package com.project.backendrestapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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

public class Manager {

    String UserName;
    String Password;

    @Id
    String ManagerId;

    @OneToOne
    private Person person;

    @ManyToOne
    private Branch branch;
    
}
