package com.project.backendrestapi.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeneficiaryResponse {
    private String responseCode;
    private String responseMessage;
    private List<BeneficiaryDto> beneficiaryDtoList;
}
