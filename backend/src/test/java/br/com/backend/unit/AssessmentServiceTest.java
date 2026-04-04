package br.com.backend.unit;

import br.com.backend.builders.entity.AssessmentBuilder;
import br.com.backend.builders.entity.ClassroomBuilder;
import br.com.backend.builders.entity.TeachingAssignmentBuilder;
import br.com.backend.dto.request.AssessmentCreateRequest;
import br.com.backend.dto.request.AssessmentUpdateRequest;
import br.com.backend.entity.*;
import br.com.backend.entity.enums.AssessmentType;
import br.com.backend.exception.BusinessException;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.repository.AssessmentRepository;
import br.com.backend.service.AssessmentService;
import br.com.backend.service.TeachingAssignmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AssessmentServiceTest {

    @Mock
    private TeachingAssignmentService teachingAssignmentService;

    @Mock
    private AssessmentRepository repository;

    @InjectMocks
    private AssessmentService service;

    private UUID assignmentId;
    private UUID assessmentId;

    private AssessmentCreateRequest createRequest;

    private Classroom classroom;
    private TeachingAssignment assignment;
    private Assessment assessment;

    @BeforeEach
    void setUp() {
        assignmentId = UUID.randomUUID();
        assessmentId = UUID.randomUUID();

        createRequest = new AssessmentCreateRequest("Prova de história", AssessmentType.PROVA, assignmentId);

        classroom = ClassroomBuilder.builder().build();
        assignment = TeachingAssignmentBuilder.builder()
                .withClassroom(classroom)
                .build();
        assessment = AssessmentBuilder.builder().build();
    }

    @Test
    void shouldRegisterAssessment() {
        when(teachingAssignmentService.findAssignmentById(createRequest.teachingAssignmentId()))
                .thenReturn(assignment);

        when(repository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        service.register(createRequest);

        verify(repository).save(any(Assessment.class));
    }

    @Test
    void shouldUpdateAssessment() {
        AssessmentUpdateRequest updateRequest = new AssessmentUpdateRequest("Trabalho de História", AssessmentType.TRABALHO);

        when(repository.findById(assessmentId)).thenReturn(Optional.of(assessment));

        service.update(assessmentId, updateRequest);

        assertEquals("Trabalho de História", assessment.getTitle());
        assertEquals(AssessmentType.TRABALHO, assessment.getType());
    }

    @Test
    void shouldDeleteAssessment() {
        when(repository.findById(assessmentId)).thenReturn(Optional.of(assessment));

        service.delete(assessmentId);

        verify(repository).findById(assessmentId);
        verify(repository).delete(assessment);
    }

    @Test
    void shouldFindAssessmentById() {
        when(repository.findById(assessmentId)).thenReturn(Optional.of(assessment));

        Assessment result = service.findActiveAssessmentById(assessmentId);

        assertEquals(assessment, result);
    }

    @Test
    void shouldThrowExceptionWhenAssessmentNotFound() {
        when(repository.findById(assessmentId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.findActiveAssessmentById(assessmentId));
    }

    @Test
    void shouldThrowExceptionWhenClassroomIsInactive() {
        when(teachingAssignmentService.findAssignmentById(assignmentId))
                .thenReturn(assignment);

        classroom.deactivate();

        assertThrows(BusinessException.class, ()-> service.register(createRequest));
    }
}
