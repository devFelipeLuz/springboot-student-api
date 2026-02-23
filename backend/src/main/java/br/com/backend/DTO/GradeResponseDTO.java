package br.com.backend.DTO;

import lombok.Getter;

import java.util.UUID;

public class GradeResponseDTO {

    @Getter
    private UUID id;

    @Getter
    private String name;

    public GradeResponseDTO(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
}
