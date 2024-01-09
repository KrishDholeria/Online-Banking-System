package com.project.backendrestapi.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Branch")
public class Branch {

    @Id
    String BranchId;
    String BranchName;
    String BranchCode;
    String Address;
    String PhoneNumber;

    @OneToMany(mappedBy = "branch")
    private List<Manager> managers;

    public List<Manager> getManagers() {
        return managers;
    }
    public void setManagers(List<Manager> managers) {
        this.managers = managers;
    }
    public Branch() {
    }
    public Branch(String branchId, String branchName, String branchCode, String address, String phoneNumber) {
        BranchId = branchId;
        BranchName = branchName;
        BranchCode = branchCode;
        Address = address;
        PhoneNumber = phoneNumber;
    }
    public String getBranchId() {
        return BranchId;
    }
    public void setBranchId(String branchId) {
        BranchId = branchId;
    }
    public String getBranchName() {
        return BranchName;
    }
    public void setBranchName(String branchName) {
        BranchName = branchName;
    }
    public String getBranchCode() {
        return BranchCode;
    }
    public void setBranchCode(String branchCode) {
        BranchCode = branchCode;
    }
    public String getAddress() {
        return Address;
    }
    public void setAddress(String address) {
        Address = address;
    }
    public String getPhoneNumber() {
        return PhoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }
    @Override
    public String toString() {
        return "Branch [BranchId=" + BranchId + ", BranchName=" + BranchName + ", BranchCode=" + BranchCode
                + ", Address=" + Address + ", PhoneNumber=" + PhoneNumber + "]";
    }

    
}
