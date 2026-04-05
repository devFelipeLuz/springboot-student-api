package br.com.backend.api.assignment;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class AssignmentData {

    private UUID id;
    private String professorName;
    private String subjectName;
    private String classroomName;
}
