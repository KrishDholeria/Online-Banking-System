
package com.project.backendrestapi.model;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Person")
// @ApiModel(description = "this table holds info related person")
public class Person {

    @Id
    private String PersonId;
    private String LastName;
    private String FirstName;
    private Date DOB;
    private String Email;
    private String PhoneNo;
    private String Address;
    private String PanNo;

    @OneToOne(mappedBy = "person")
    private Manager manager;

    @OneToOne(mappedBy = "person")
    private Customer customer;

    @OneToOne(mappedBy = "person")
    private Admin admin;

    public Person(String personId, String lastName, String firstName, Date dOB, String email, String phoneNo,
            String address, String panNo, Manager manager) {
        PersonId = personId;
        LastName = lastName;
        FirstName = firstName;
        DOB = dOB;
        Email = email;
        PhoneNo = phoneNo;
        Address = address;
        PanNo = panNo;
        this.manager = manager;
    }
    public Manager getManager() {
        return manager;
    }
    public void setManager(Manager manager) {
        this.manager = manager;
    }
    public Person() {
    }
    public Person(String personId, String lastName, String firstName, Date dOB, String email, String phoneNo,
            String address, String panNo) {
        PersonId = personId;
        LastName = lastName;
        FirstName = firstName;
        DOB = dOB;
        Email = email;
        PhoneNo = phoneNo;
        Address = address;
        PanNo = panNo;
    }
    public String getPersonId() {
        return PersonId;
    }
    public void setPersonId(String personId) {
        PersonId = personId;
    }
    public String getLastName() {
        return LastName;
    }
    public void setLastName(String lastName) {
        LastName = lastName;
    }
    public String getFirstName() {
        return FirstName;
    }
    public void setFirstName(String firstName) {
        FirstName = firstName;
    }
    public Date getDOB() {
        return DOB;
    }
    public void setDOB(Date dOB) {
        DOB = dOB;
    }
    public String getEmail() {
        return Email;
    }
    public void setEmail(String email) {
        Email = email;
    }
    public String getPhoneNo() {
        return PhoneNo;
    }
    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }
    public String getAddress() {
        return Address;
    }
    public void setAddress(String address) {
        Address = address;
    }
    public String getPanNo() {
        return PanNo;
    }
    public void setPanNo(String panNo) {
        PanNo = panNo;
    }
    @Override
    public String toString() {
        return "Person [PersonId=" + PersonId + ", LastName=" + LastName + ", FirstName=" + FirstName + ", DOB=" + DOB
                + ", Email=" + Email + ", PhoneNo=" + PhoneNo + ", Address=" + Address + ", PanNo=" + PanNo + "]";
    }
    
}
