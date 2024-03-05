package com.project.backendrestapi.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {
    private Double accountBalance;
    private Long cutomerId;
    private Long branchId;
}
