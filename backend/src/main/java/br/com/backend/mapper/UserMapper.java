package br.com.backend.mapper;

import br.com.backend.dto.response.UserResponseDTO;
import br.com.backend.entity.User;

public final class UserMapper {

    private UserMapper() {
        throw new UnsupportedOperationException("Mapper");
    }

    public static UserResponseDTO toDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );
    }
}
