package com.project.backendrestapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Admin {
    
    @Id
    String AdminId;

    @OneToOne
    private Person person;
}
