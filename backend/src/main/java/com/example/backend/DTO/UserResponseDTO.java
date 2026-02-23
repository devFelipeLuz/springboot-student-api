package com.example.backend.DTO;

import com.example.backend.entity.User.Role;
import lombok.Getter;

import java.util.UUID;

public class UserResponseDTO {

    @Getter
    private UUID id;

    @Getter
    private String username;

    @Getter
    private Role role;

    public UserResponseDTO(UUID id, String username, Role role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }
}
