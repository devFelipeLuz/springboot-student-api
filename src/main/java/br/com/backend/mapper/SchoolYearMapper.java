package br.com.backend.mapper;

import br.com.backend.dto.response.SchoolYearResponseDTO;
import br.com.backend.entity.SchoolYear;

public class SchoolYearMapper {

    private SchoolYearMapper() {
        throw new UnsupportedOperationException("Mapper");
    }

    public static SchoolYearResponseDTO toDTO(SchoolYear schoolYear) {
        return new SchoolYearResponseDTO(
                schoolYear.getId(),
                schoolYear.getYear(),
                schoolYear.getStartDate()
        );
    }
}
