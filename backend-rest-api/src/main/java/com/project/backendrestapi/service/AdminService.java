package com.project.backendrestapi.service;

import com.project.backendrestapi.dto.AdminDto;
import com.project.backendrestapi.model.*;
import com.project.backendrestapi.repository.AdminRepository;
import com.project.backendrestapi.repository.BranchRepository;
import com.project.backendrestapi.repository.CustomerRepository;
import com.project.backendrestapi.repository.ManagerRepository;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminService {

    @Autowired
    private final AdminRepository adminRepository;

    @Autowired
    private final ManagerRepository managerRepository;

    @Autowired
    private final CustomerRepository customerRepository;

    @Autowired
    private final BranchRepository branchRepository;

    @Autowired
    private final PersonService personService;

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Optional<Admin> getAdminById(Long adminId) {
        return adminRepository.findById(adminId);
    }

    public Admin createAdmin(AdminDto admin) {
        Person person = personService.createPerson(admin.getPerson());
        Admin admin1 = Admin.builder()
                .userName(admin.getUserName())
                .password(admin.getPassword())
                .person(person)
                .build();
        return adminRepository.save(admin1);
    }

    public Optional<Admin> updateAdmin(Long adminId, AdminDto updatedAdmin) {
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

    public boolean deleteBranch(Long branchId) {
        if (branchRepository.existsById(branchId)) {
            branchRepository.deleteById(branchId);
            return true;
        } else {
            return false;
        }
    }

    public AdminDto entityToDto(Admin admin){
        return AdminDto.builder()
                .userName(admin.getUserName())
                .password(admin.getPassword())
                .person(personService.entityToDto(admin.getPerson()))
                .build();
    }
}
