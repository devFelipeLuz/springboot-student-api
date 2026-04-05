package br.com.backend.mapper;

import br.com.backend.dto.response.ProfessorResponseDTO;
import br.com.backend.entity.Professor;
import br.com.backend.entity.User;

public final class ProfessorMapper {

    private ProfessorMapper() {
        throw new UnsupportedOperationException("Mapper");
    }

    public static ProfessorResponseDTO toDTO(Professor professor) {
        User user = professor.getUser();

        return new ProfessorResponseDTO(
                professor.getId(),
                professor.getName(),
                user.getUsername()
        );
    }
}
