package br.com.backend.mapper;

import br.com.backend.dto.response.TeachingAssignmentResponseDTO;
import br.com.backend.entity.TeachingAssignment;

public class TeachingAssignmentMapper {

    private TeachingAssignmentMapper() {
        throw new UnsupportedOperationException("Mapper");
    }

    public static TeachingAssignmentResponseDTO toDTO(TeachingAssignment assignment) {
        return new TeachingAssignmentResponseDTO(
                assignment.getId(),
                assignment.getProfessor().getName(),
                assignment.getSubject().getName(),
                assignment.getClassroom().getName());
    }
}
