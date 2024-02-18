package com.project.backendrestapi.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CustomerSignupDto {
    private String userName;
    private String accountNo;
    private String branchCode;
}
