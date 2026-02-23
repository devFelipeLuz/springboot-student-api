package br.com.backend.DTO;

import br.com.backend.domain.User.Role;
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
