package br.com.backend.unit;

import br.com.backend.builders.entity.ClassroomBuilder;
import br.com.backend.builders.entity.EnrollmentBuilder;
import br.com.backend.builders.entity.SchoolYearBuilder;
import br.com.backend.builders.entity.StudentBuilder;
import br.com.backend.dto.request.EnrollmentRequest;
import br.com.backend.entity.*;
import br.com.backend.exception.BusinessException;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.repository.EnrollmentRepository;
import br.com.backend.service.ClassroomService;
import br.com.backend.service.EnrollmentService;
import br.com.backend.service.SchoolYearService;
import br.com.backend.service.StudentService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnrollmentServiceTest {

    @Mock
    private StudentService studentService;

    @Mock
    private SchoolYearService schoolYearService;

    @Mock
    private ClassroomService classroomService;

    @Mock
    private EnrollmentRepository repository;

    @InjectMocks
    private EnrollmentService service;


    private Student student;
    private SchoolYear schoolYear;
    private Classroom classroom;
    private Enrollment enrollment;

    private EnrollmentRequest request;

    private UUID studentId;
    private UUID schoolYearId;
    private UUID classroomId;
    private UUID enrollmentId;


    @BeforeEach
    void setUp() {
        studentId = UUID.randomUUID();
        schoolYearId = UUID.randomUUID();
        classroomId = UUID.randomUUID();
        enrollmentId = UUID.randomUUID();

        student = StudentBuilder.builder().build();
        schoolYear = SchoolYearBuilder.builder().build();
        classroom = ClassroomBuilder.builder().build();
        enrollment = EnrollmentBuilder.builder()
                .withStudent(student)
                .withSchoolYear(schoolYear)
                .withClassroom(classroom)
                .withId(enrollmentId)
                .build();

        request = new EnrollmentRequest(studentId, schoolYearId, classroomId);
    }

    @Test
    void shouldEnrollStudentSuccessfully() {
        when(studentService.findActiveStudentById(studentId)).thenReturn(student);
        when(schoolYearService.findActiveSchoolYear(schoolYearId)).thenReturn(schoolYear);
        when(classroomService.findActiveClassroomById(classroomId)).thenReturn(classroom);
        when(repository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        service.enroll(request);

        verify(repository).save(any(Enrollment.class));
    }

    @Test
    void shouldThrowExceptionWhenClassroomIsFull() {
        when(studentService.findActiveStudentById(studentId)).thenReturn(student);
        when(schoolYearService.findActiveSchoolYear(schoolYearId)).thenReturn(schoolYear);
        when(classroomService.findActiveClassroomById(classroomId)).thenReturn(classroom);

        classroom.changeCapacity(1);
        classroom.increaseActiveEnrollmentsCount();

        assertThrows(BusinessException.class, () -> service.enroll(request));
    }

    @Test
    void shouldCancelEnrollment() {
        when(repository.findById(enrollmentId))
                .thenReturn(Optional.of(enrollment));

        enrollment.register();

        service.cancel(enrollmentId);

        assertTrue(enrollment.isCancelled());
    }

    @Test
    void shouldFinishEnrollment() {
        Enrollment enrollment = new Enrollment(student, schoolYear, classroom);

        when(repository.findById(enrollment.getId())).thenReturn(Optional.of(enrollment));

        service.finishEnrollment(enrollment.getId());

        assertTrue(enrollment.isFinished());
    }

    @Test
    void shouldThrowExceptionWhenEnrollmentNotFound() {
        UUID enrollmentId = UUID.randomUUID();

        when(repository.findById(enrollmentId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, ()-> service.cancel(enrollmentId));
    }

    @Test
    void shouldThrowExceptionWhenStudentIsInactive() {
        when(repository.findById(enrollmentId)).thenReturn(Optional.of(enrollment));
        enrollment.getStudent().deactivate();

        assertThrows(BusinessException.class, ()-> service.cancel(enrollmentId));
    }

    @Test
    void shouldNotSaveEnrollmentWhenClassroomIsFull() {
        when(studentService.findActiveStudentById(studentId)).thenReturn(student);
        when(schoolYearService.findActiveSchoolYear(schoolYearId)).thenReturn(schoolYear);
        when(classroomService.findActiveClassroomById(classroomId)).thenReturn(classroom);

        classroom.changeCapacity(1);
        classroom.increaseActiveEnrollmentsCount();

        assertThrows(BusinessException.class, () -> service.enroll(request));

        verify(repository, never()).save(any());
    }
}
