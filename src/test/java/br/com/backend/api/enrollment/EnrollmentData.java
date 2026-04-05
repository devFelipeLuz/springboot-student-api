package br.com.backend.api.enrollment;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class EnrollmentData {

    private UUID id;

    private String studentName;
    private String studentEmail;

    private Integer schoolYear;

    private String classroomName;
}
