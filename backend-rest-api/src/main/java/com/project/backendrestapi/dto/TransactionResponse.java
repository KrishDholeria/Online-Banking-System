package com.project.backendrestapi.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {
    private String refId;
    private String amount;
    private String type;
    private String accountTo;
    private String accountFrom;

}
