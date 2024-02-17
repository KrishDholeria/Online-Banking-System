package com.project.backendrestapi.controller;

import com.project.backendrestapi.model.Admin;
import com.project.backendrestapi.model.Branch;
import com.project.backendrestapi.model.Customer;
import com.project.backendrestapi.model.Manager;
import com.project.backendrestapi.service.AdminService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/all")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        List<Admin> admins = adminService.getAllAdmins();
        return ResponseEntity.ok(admins);
    }

    @GetMapping("/{adminId}")
    public ResponseEntity<Admin> getAdminById(@PathVariable Long adminId) {
        Optional<Admin> admin = adminService.getAdminById(adminId);
        return admin.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/new")
    public ResponseEntity<Admin> createAdmin(@RequestBody Admin admin) {
        Admin createdAdmin = adminService.createAdmin(admin);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAdmin);
    }

    @PutMapping("/{adminId}")
    public ResponseEntity<Admin> updateAdmin(@PathVariable Long adminId, @RequestBody Admin updatedAdmin) {
        Optional<Admin> admin = adminService.updateAdmin(adminId, updatedAdmin);
        return admin.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{adminId}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long adminId) {
        boolean deleted = adminService.deleteAdmin(adminId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginAdmin(@RequestBody Admin admin) {
        if (adminService.authenticateAdmin(admin.getUserName(), admin.getPassword())) {
            // If authentication is successful, generate a JWT token
            String token = generateToken(admin.getUserName());
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    // Method to generate JWT token
    private String generateToken(String username) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(new Date(nowMillis + 86400000)) // Token expires in 24 hours
                .signWith(key)
                .compact();
    }

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = adminService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/managers")
    public ResponseEntity<List<Manager>> getAllManagers() {
        List<Manager> managers = adminService.getAllManagers();
        return ResponseEntity.ok(managers);
    }

    @GetMapping("/managers/{managerId}")
    public ResponseEntity<Manager> getManagerById(@PathVariable Long managerId) {
        Optional<Manager> manager = adminService.getManagerById(managerId);
        return manager.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/managers/new")
    public ResponseEntity<Manager> createManager(@RequestBody Manager manager) {
        manager.setUserName(manager.getUserName());

        Manager createdManager = adminService.createManager(manager);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdManager);
    }

    // @PutMapping("/managers/{managerId}")
    // public ResponseEntity<Manager> updateManager(@PathVariable Long managerId,
    // @RequestBody Manager updatedManager) {
    // Optional<Manager> manager = adminService.updateManager(managerId,
    // updatedManager);
    // return manager.map(ResponseEntity::ok).orElseGet(() ->
    // ResponseEntity.notFound().build());
    // }

    @DeleteMapping("/managers/{managerId}")
    public ResponseEntity<Void> deleteManager(@PathVariable Long managerId) {
        boolean deleted = adminService.deleteManager(managerId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/branches")
    public ResponseEntity<List<Branch>> getAllBranches() {
        List<Branch> branches = adminService.getAllBranches();
        return ResponseEntity.ok(branches);
    }

    @GetMapping("/branches/{branchId}")
    public ResponseEntity<Branch> getBranchById(@PathVariable Long branchId) {
        Optional<Branch> branch = adminService.getBranchById(branchId);
        return branch.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/branches/new")
    public ResponseEntity<Branch> createBranch(@RequestBody Branch branch) {
        Branch createdBranch = adminService.createBranch(branch);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBranch);
    }

    @PutMapping("/branches/{branchId}")
    public ResponseEntity<Branch> updateBranch(@PathVariable Long branchId, @RequestBody Branch updatedBranch) {
        Optional<Branch> branch = adminService.updateBranch(branchId, updatedBranch);
        return branch.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/branches/{branchId}")
    public ResponseEntity<Void> deleteBranch(@PathVariable Long branchId) {
        boolean deleted = adminService.deleteBranch(branchId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}