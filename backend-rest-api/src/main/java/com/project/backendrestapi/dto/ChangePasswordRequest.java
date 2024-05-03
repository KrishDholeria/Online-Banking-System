package com.project.backendrestapi.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordRequest {
    private String username;
    private String oldPassword;
    private String newPassword;
    

}
