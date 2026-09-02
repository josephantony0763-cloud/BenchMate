package com.antony.benchmate.dto.response;

public class LoginResponse {

    private Integer userId;
    private String name;
    private String email;
    private String role;
    private Integer classId;
    private String message;
    private String token;

    public LoginResponse(
            Integer userId,
            String name,
            String email,
            String role,
            Integer classId,
            String token,
            String message) {

        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.classId = classId;
        this.token = token;
        this.message = message;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public Integer getClassId() {
        return classId;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }
}