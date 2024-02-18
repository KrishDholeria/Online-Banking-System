package com.project.backendrestapi.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto {
    private String userName;
    private String password;
    private String panNo;
    private PersonDto person; // Include PersonDto
    private AccountDto account; // Include AccountDto
}
