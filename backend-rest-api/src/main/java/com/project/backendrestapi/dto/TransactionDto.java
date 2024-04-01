package com.project.backendrestapi.dto;

import java.util.List;
import java.util.ArrayList;

import com.project.backendrestapi.model.Transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private String transactionType;
    private String amount;
    private String accountNo;

}
