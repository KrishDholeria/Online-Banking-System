package com.project.backendrestapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private String userName;
    private String password;
    private String panNo;
    private PersonDto person; // Include PersonDto
    private AccountDto account; // Include AccountDto
}
