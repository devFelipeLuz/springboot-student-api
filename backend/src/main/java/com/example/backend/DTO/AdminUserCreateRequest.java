package com.example.backend.DTO;

import com.example.backend.entity.User.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public class AdminUserCreateRequest {

    @Getter
    @Setter
    @NotBlank(message = "Username is required")
    private String username;

    @Getter
    @Setter
    @NotBlank(message = "password is required")
    private String password;

    @Getter
    @Setter
    private Role role;
}
