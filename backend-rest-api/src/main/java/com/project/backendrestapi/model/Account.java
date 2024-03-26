package com.project.backendrestapi.model;

import java.util.ArrayList;
import java.util.Date;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long accountId;
    private String accountNumber;
    private Double accountBalance;
    private Date dateOpened;
    private Date dateClosed;

    @OneToOne(mappedBy = "account")
    Customer customer;

    @ManyToOne
    Branch branch;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Transaction> transactions;

}
