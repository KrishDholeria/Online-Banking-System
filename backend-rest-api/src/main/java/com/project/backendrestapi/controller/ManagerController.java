package com.project.backendrestapi.controller;

import com.project.backendrestapi.dto.LoginResponse;
import com.project.backendrestapi.dto.ManagerDto;
import com.project.backendrestapi.model.Manager;
import com.project.backendrestapi.service.ManagerService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class ManagerController {

    private final ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @PostMapping("/manager/login")
    public ResponseEntity<?> managerLogin(@RequestBody ManagerDto managerDto) {
        // Extract username and password from the request body
        String username = managerDto.getUserName();
        String password = managerDto.getPassword();

        // Authenticate manager
        Manager manager = managerService.authenticateManager(username, password);

        if (manager != null) {
            // If authentication is successful, generate JWT token
            String token = generateToken(username);

            // Return the token in the response body
            return ResponseEntity.ok(new LoginResponse("Login successful", token));
        } else {
            // If authentication fails, return an unauthorized response
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse("Invalid username or password"));
        }
    }

    // Generate JWT token
    private String generateToken(String username) {
        long expirationTime = 1000 * 60 * 60 * 10; // Token expiration time (10 hours)
        String secretKey = "YcoufefeffrsdsgerererergrrgegerSefffbfbrrrrcoufefeffrsdsgerererergrrgegerSefffbfbrrrrcoufefeffrsdsgerererergrrgegerSefffbfbrrrrcoufefeffrsdsgerererergrrgegerSefffbfbrrrrcoufefeffrsdsgerererergrrgegerSefffbfbrrrrcoufefeffrsdsgerererergrrgegerSefffbfbrrrrcoufefeffrsdsgerererergrrgegerSefffbfbrrrrcretKdvvvvfvfereydwdeffwewwff"; // Change this to your secret key
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }
}
