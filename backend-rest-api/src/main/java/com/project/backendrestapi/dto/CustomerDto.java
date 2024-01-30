package com.project.backendrestapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private String userName;
    private String password;
    private String panNo;
    private Long personId;
    private String lastName;
    private String firstName;
    private String dob;
    private String email;
    private String phoneNo;
    private String address;
}
