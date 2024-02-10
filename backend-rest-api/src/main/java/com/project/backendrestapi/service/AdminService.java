package com.project.backendrestapi.service;

import com.project.backendrestapi.model.Admin;
import com.project.backendrestapi.repository.AdminRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
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
            // You may update other properties as needed

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
}
