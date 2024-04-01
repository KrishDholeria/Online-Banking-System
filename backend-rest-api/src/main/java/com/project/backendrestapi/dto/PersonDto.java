package com.project.backendrestapi.dto;

import java.sql.Date;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonDto {
    
    private String lastName;
    private String firstName;
    private String dob;
    private String email;
    private String phoneNo;
    private String address;
}
