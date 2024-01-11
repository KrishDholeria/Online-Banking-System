package com.project.backendrestapi.model;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String TransactionId;
    String TransactionType;
    String Amount;
    Date TransactionDate;

    @ManyToOne
    private Branch branch;
    
    @OneToOne
    private Transaction relatedTransaction;

    @ManyToOne
    private Account account;
}
