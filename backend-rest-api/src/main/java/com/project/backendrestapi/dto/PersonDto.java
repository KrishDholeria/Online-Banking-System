package com.project.backendrestapi.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {
    
    private String lastName;
    private String firstName;
    private Date dob;
    private String email;
    private String phoneNo;
    private String address;
}
