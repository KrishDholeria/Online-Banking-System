package com.project.backendrestapi.service;

import com.project.backendrestapi.model.Customer;
import com.project.backendrestapi.model.Manager;
import com.project.backendrestapi.model.Admin;
import com.project.backendrestapi.model.Branch;
import com.project.backendrestapi.repository.AdminRepository;
import com.project.backendrestapi.repository.BranchRepository;
import com.project.backendrestapi.repository.CustomerRepository;
import com.project.backendrestapi.repository.ManagerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private final AdminRepository adminRepository;

    @Autowired
    private final ManagerRepository managerRepository;

    @Autowired
    private final CustomerRepository customerRepository;

    @Autowired
    private final BranchRepository branchRepository;

    public AdminService(AdminRepository adminRepository, ManagerRepository managerRepository,
            CustomerRepository customerRepository, BranchRepository branchRepository) {
        this.adminRepository = adminRepository;
        this.managerRepository = managerRepository;
        this.customerRepository = customerRepository;
        this.branchRepository = branchRepository;
    }

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Optional<Admin> getAdminById(Long adminId) {
        return adminRepository.findById(adminId);
    }

    public Admin createAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    public Optional<Admin> updateAdmin(Long adminId, Admin updatedAdmin) {
        Optional<Admin> existingAdmin = adminRepository.findById(adminId);

        if (existingAdmin.isPresent()) {
            Admin admin = existingAdmin.get();
            admin.setUserName(updatedAdmin.getUserName());
            admin.setPassword(updatedAdmin.getPassword());

            return Optional.of(adminRepository.save(admin));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteAdmin(Long adminId) {
        if (adminRepository.existsById(adminId)) {
            adminRepository.deleteById(adminId);
            return true;
        } else {
            return false;
        }
    }

    public boolean authenticateAdmin(String username, String password) {

        Optional<Admin> optionalAdmin = adminRepository.findByUserName(username);

        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();
            return admin.getPassword().equals(password);
        } else {
            return false;
        }
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    public Optional<Manager> getManagerById(Long managerId) {
        return managerRepository.findById(managerId);
    }

    public Manager createManager(Manager manager) {
        return managerRepository.save(manager);
    }

    // public Optional<Manager> updateManager(Long managerId, Manager
    // updatedManager) {
    // Optional<Manager> existingManager = managerRepository.findById(managerId);

    // if (existingManager.isPresent()) {
    // Manager manager = existingManager.get();
    // manager.setFirstName(updatedManager.getFirstName());
    // manager.setLastName(updatedManager.getLastName());
    // // Update other properties as needed

    // return Optional.of(managerRepository.save(manager));
    // } else {
    // return Optional.empty();
    // }
    // }

    public boolean deleteManager(Long managerId) {
        if (managerRepository.existsById(managerId)) {
            managerRepository.deleteById(managerId);
            return true;
        } else {
            return false;
        }
    }

    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    public Optional<Branch> getBranchById(Long branchId) {
        return branchRepository.findById(branchId);
    }

    public Branch createBranch(Branch branch) {
        return branchRepository.save(branch);
    }

    public Optional<Branch> updateBranch(Long branchId, Branch updatedBranch) {
        Optional<Branch> existingBranch = branchRepository.findById(branchId);

        if (existingBranch.isPresent()) {
            Branch branch = existingBranch.get();
            branch.setBranchName(updatedBranch.getBranchName());
            branch.setBranchCode(updatedBranch.getBranchCode());
            branch.setAddress(updatedBranch.getAddress());
            branch.setPhoneNumber(updatedBranch.getPhoneNumber());
            return Optional.of(branchRepository.save(branch));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteBranch(Long branchId) {
        if (branchRepository.existsById(branchId)) {
            branchRepository.deleteById(branchId);
            return true;
        } else {
            return false;
        }
    }
}
