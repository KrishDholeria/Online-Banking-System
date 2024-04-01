package com.project.backendrestapi.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BranchDto {
    
    private String branchName;
    private String address;
    private String phoneNumber;
    private String branchCode;
}
