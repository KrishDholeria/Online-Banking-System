package com.project.backendrestapi.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto {
    private String responseCode;
    private String responseMessage;
    private String userName;
    private String password;
    private String panNo;
    private PersonDto person; // Include PersonDto
    private AccountDto account; // Include AccountDto
    private List<BeneficiaryDto> beneficiaries;
}
