package com.project.backendrestapi.controller;

import com.project.backendrestapi.dto.JwtRequest;
import com.project.backendrestapi.dto.JwtResponse;
import com.project.backendrestapi.security.JwtHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.security.Security;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manager;


    @Autowired
    private JwtHelper helper;



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest request) {

        try {
            this.doAuthenticate(request.getUsername(), request.getPassword());
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Credentials Invalid", HttpStatus.UNAUTHORIZED);
        }


        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = this.helper.generateToken(userDetails);

        JwtResponse response = JwtResponse.builder()
                .token(token)
                .username(userDetails.getUsername()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/status/{token}")
    public ResponseEntity<?> getStatus(@PathVariable String token){
//        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        try{
            Boolean res = helper.isTokenExpired(token);
            return new ResponseEntity<>(!res, HttpStatus.OK);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }

    private void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication);


        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }

    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid !!";
    }


}
