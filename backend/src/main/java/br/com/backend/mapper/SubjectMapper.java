package br.com.backend.mapper;

import br.com.backend.dto.response.SubjectResponseDTO;
import br.com.backend.entity.Subject;

public class SubjectMapper {

    private SubjectMapper() {
        throw new UnsupportedOperationException("Mapper");
    }

    public static SubjectResponseDTO toDTO(Subject subject) {
        return new SubjectResponseDTO(
                subject.getId(),
                subject.getName());
    }
}
