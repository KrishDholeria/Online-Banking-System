package com.project.backendrestapi.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminDto {
    private String userName;
    private String password;
    private PersonDto person;
}
