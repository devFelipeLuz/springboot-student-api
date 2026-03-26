package br.com.backend.api.assessment;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class AssessmentData {

    private UUID id;
    private String title;
    private String type;
    private UUID assignmentId;
}
