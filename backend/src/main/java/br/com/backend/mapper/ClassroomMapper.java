package br.com.backend.mapper;

import br.com.backend.dto.response.ClassroomResponseDTO;
import br.com.backend.entity.Classroom;

public final class ClassroomMapper {

    private ClassroomMapper() {
        throw new UnsupportedOperationException("Mapper");
    }

    public static ClassroomResponseDTO toDTO(Classroom classroom) {
        return new ClassroomResponseDTO(
                classroom.getId(),
                classroom.getName());
    }
}
