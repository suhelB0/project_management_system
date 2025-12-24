package com.example.projectmanagement.dto;

import jakarta.validation.constraints.NotBlank;

public class UserRoleRequest {
    @NotBlank(message = "username is not blank")
    private String username;

    @NotBlank(message = "role is not blank")
    private String roleName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
