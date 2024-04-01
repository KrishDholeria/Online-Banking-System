package com.project.backendrestapi.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OtpRequest {
    private String otp;
}
