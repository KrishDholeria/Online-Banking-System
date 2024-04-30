package com.project.backendrestapi.model;

import java.util.Date;

import jakarta.persistence.*;
//import jakarta.persistence.OneToMany;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long transactionId;
    private String referenceId;
    private String transactionType;
    private int amount;
    private Date transactionDate;

    // @ManyToOne
    // private Branch branch;

    @OneToOne
    private Transactions relatedTransactions;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account account;
}
