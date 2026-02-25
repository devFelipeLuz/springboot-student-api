package br.com.backend.DTO;

import lombok.Getter;

import java.util.UUID;

@Getter
public class GradeResponseDTO {

    private UUID id;

    private String name;

    public GradeResponseDTO(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
}
