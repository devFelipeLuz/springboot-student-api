package br.com.backend.mapper;

import br.com.backend.dto.response.TeachingAssignmetResponseDTO;
import br.com.backend.entity.TeachingAssignment;

public class TeachingAssignmentMapper {

    private TeachingAssignmentMapper() {
        throw new UnsupportedOperationException("Mapper");
    }

    public static TeachingAssignmetResponseDTO toDTO(TeachingAssignment assignment) {
        return new TeachingAssignmetResponseDTO(
                assignment.getId(),
                assignment.getProfessor().getName(),
                assignment.getSubject().getName(),
                assignment.getClassroom().getName());
    }
}
