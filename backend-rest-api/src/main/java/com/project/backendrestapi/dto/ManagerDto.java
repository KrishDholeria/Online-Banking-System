package com.project.backendrestapi.dto;

import com.project.backendrestapi.model.Branch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ManagerDto {
    private String userName;
    private String password;
    private Long branchId;
    private PersonDto person;
}
