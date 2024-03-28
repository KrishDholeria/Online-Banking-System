package com.project.backendrestapi.controller;

import com.project.backendrestapi.dto.AdminDto;
import com.project.backendrestapi.dto.BranchDto;
import com.project.backendrestapi.dto.ChangePasswordRequest;
import com.project.backendrestapi.dto.ManagerDto;
import com.project.backendrestapi.model.Admin;
import com.project.backendrestapi.model.Branch;
import com.project.backendrestapi.model.Customer;
import com.project.backendrestapi.model.Manager;
import com.project.backendrestapi.service.AdminService;

import com.project.backendrestapi.service.BranchService;
import com.project.backendrestapi.service.ManagerService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private BranchService branchService;

    @GetMapping("/all")
    public ResponseEntity<List<AdminDto>> getAllAdmins() {
        List<Admin> admins = adminService.getAllAdmins();
        List<AdminDto> adminDtos = new ArrayList<>();
        for (Admin a : admins) {
            adminDtos.add(adminService.entityToDto(a));
        }
        return ResponseEntity.ok(adminDtos);
    }

    @GetMapping("/{adminId}")
    public ResponseEntity<AdminDto> getAdminById(@PathVariable Long adminId) {
        Optional<Admin> admin = adminService.getAdminById(adminId);
        return admin.map(value -> ResponseEntity.status(HttpStatus.OK).body(adminService.entityToDto(admin.get())))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/new")
    public ResponseEntity<AdminDto> createAdmin(@RequestBody AdminDto admin) {
        Admin createdAdmin = adminService.createAdmin(admin);
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.entityToDto(createdAdmin));
    }

    @PutMapping("/{adminId}")
    public ResponseEntity<AdminDto> updateAdmin(@PathVariable Long adminId, @RequestBody AdminDto updatedAdmin) {
        Optional<Admin> admin = adminService.updateAdmin(adminId, updatedAdmin);
        return admin.map(value -> ResponseEntity.status(HttpStatus.OK).body(adminService.entityToDto(admin.get())))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{adminId}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long adminId) {
        boolean deleted = adminService.deleteAdmin(adminId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/admin/login")
    public ResponseEntity<String> loginAdmin(@RequestBody AdminDto admin) {
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

    // @GetMapping("/customers")
    // public ResponseEntity<List<Customer>> getAllCustomers() {
    // List<Customer> customers = adminService.getAllCustomers();
    // return ResponseEntity.ok(customers);
    // }

    @GetMapping("all/managers")
    public ResponseEntity<List<ManagerDto>> getAllManagers() {
        List<Manager> managers = managerService.getAllManagers();
        List<ManagerDto> managerDtos = new ArrayList<>();
        for (Manager m : managers) {
            managerDtos.add(managerService.entityToDto(m));
        }
        return ResponseEntity.ok(managerDtos);
    }

    @GetMapping("/manager/username")
    public ResponseEntity<ManagerDto> getManagerById(@RequestParam String userName) {
        Optional<Manager> manager = managerService.getManagerByuserName(userName);
        return manager
                .map(value -> ResponseEntity.status(HttpStatus.OK).body(managerService.entityToDto(manager.get())))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add/manager")
    public ResponseEntity<?> createManager(@RequestBody ManagerDto manager) {
        Manager createdManager = managerService.createManager(manager);
        if (createdManager != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(managerService.entityToDto(createdManager));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        }
    }

    @DeleteMapping("managers/{username}")
    public ResponseEntity<?> deleteManagerByUsername(@PathVariable String username) {
        try {
            managerService.deleteManagerByUsername(username);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("Failed to delete manager: " + e.getMessage());
        }
    }

    @GetMapping("/branches")
    public ResponseEntity<List<BranchDto>> getAllBranches() {
        List<Branch> branches = branchService.getAllBranches();
        List<BranchDto> branchDtos = new ArrayList<>();
        for (Branch b : branches) {
            branchDtos.add(branchService.entityToDto(b));
        }
        return ResponseEntity.ok(branchDtos);
    }

    @GetMapping("/branches/{branchCode}")
    public ResponseEntity<BranchDto> getBranchById(@PathVariable String branchCode) {
        Optional<Branch> branch = branchService.getBranchByBranchCode(branchCode);
        return branch.map(value -> ResponseEntity.status(HttpStatus.OK).body(branchService.entityToDto(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add/branch")
    public ResponseEntity<BranchDto> createBranch(@RequestBody BranchDto branch) {
        Branch createdBranch = branchService.createBranch(branch);
        return ResponseEntity.status(HttpStatus.CREATED).body(branchService.entityToDto(createdBranch));
    }

    @PutMapping("/branches/{branchCode}")
    public ResponseEntity<BranchDto> updateBranch(@PathVariable String branchCode,
            @RequestBody BranchDto updatedBranch) {
        System.out.println(branchCode);
        Optional<Branch> branch = branchService.updateBranch(branchCode, updatedBranch);
        if (branch.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(branchService.entityToDto(branch.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/branches/{branchCode}")
    public ResponseEntity<Void> deleteBranch(@PathVariable String branchCode) {
        boolean deleted = branchService.deleteBranch(branchCode);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("admin/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {
        try {
            String username = request.getUsername();
            String oldPassword = request.getOldPassword();
            String newPassword = request.getNewPassword();

            // Validate old password
            boolean isPasswordValid = adminService.validatePassword(username, oldPassword);
            if (!isPasswordValid) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid old password");
            }
            // Change password
            adminService.changePassword(username, newPassword);

            return ResponseEntity.ok().body("Password changed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to change password: " + e.getMessage());
        }
    }

}