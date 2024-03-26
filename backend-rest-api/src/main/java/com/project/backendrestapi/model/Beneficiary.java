package com.project.backendrestapi.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Beneficiary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long beneficiaryId;
    private String beneficiaryName;
    private String accountNumber;
    private String branchCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

}
