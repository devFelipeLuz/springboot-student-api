package br.com.backend.api.professor;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ProfessorData {

    private UUID id;
    private String name;
    private String email;
    private String password;
}
