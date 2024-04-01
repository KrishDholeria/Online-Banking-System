package com.project.backendrestapi.dto;

import com.project.backendrestapi.model.Branch;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManagerDto {
    private Long manageId;
    private String userName;
    private String password;
    private BranchDto branch;
    private PersonDto person;
}
