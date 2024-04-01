package com.project.backendrestapi.dto;

public class LoginResponse {

    private String message;
    private String token;

    // Default constructor
    public LoginResponse() {
    }

    // Constructor with message and token parameters
    public LoginResponse(String message, String token) {
        this.message = message;
        this.token = token;
    }

    public LoginResponse(String message) {
        this.message = message;
    }

    // Getters and setters for message and token
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
