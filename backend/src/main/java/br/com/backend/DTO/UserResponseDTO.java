package br.com.backend.DTO;

import br.com.backend.domain.Role;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UserResponseDTO {

    private UUID id;

    private String username;

    private Role role;

    public UserResponseDTO(UUID id, String username, Role role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }
}
