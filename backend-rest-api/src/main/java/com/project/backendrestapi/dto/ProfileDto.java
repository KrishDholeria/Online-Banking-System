package com.project.backendrestapi.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProfileDto {
    private String userName;
    private String panNo;
    private PersonDto person; 
    private Double balance;
    private String accNo;
    private String ifscCode;
    private String branchAddress;
    private String branchName;
    private String phoneNo;
}
