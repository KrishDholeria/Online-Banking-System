package com.project.backendrestapi.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonDto {
    
    private String lastName;
    private String firstName;
    private String dob;
    private String email;
    private String phoneNo;
    private String address;

//    public PersonDto dob(Date date){
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        this.setDob(sdf.format(date));
//        return this;
//    }
}
