package com.project.backendrestapi.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BeneficiaryDto {
    private String beneficiaryName;
    private String accountNo;
    private String branchCode;
}
