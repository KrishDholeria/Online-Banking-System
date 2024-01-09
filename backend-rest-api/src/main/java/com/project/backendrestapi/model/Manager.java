package com.project.backendrestapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Manager {

    @Id
    String ManagerId;

    @OneToOne
    private Person person;

    @ManyToOne
    private Branch branch;

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Manager(String managerId) {
        ManagerId = managerId;
    }

    public Manager() {
    }

    public Manager(String managerId, Person person) {
        ManagerId = managerId;
        this.person = person;
    }

    public String getManagerId() {
        return ManagerId;
    }

    public void setManagerId(String managerId) {
        ManagerId = managerId;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "Manager [ManagerId=" + ManagerId + ", person=" + person + "]";
    }



    
}
