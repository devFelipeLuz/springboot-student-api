package br.com.backend.unit.service;

import br.com.backend.builders.AttendanceRecordBuilder;
import br.com.backend.builders.AttendanceSessionBuilder;
import br.com.backend.builders.EnrollmentBuilder;
import br.com.backend.builders.TeachingAssignmentBuilder;
import br.com.backend.dto.request.AttendanceCreateRequest;
import br.com.backend.dto.request.AttendanceRecordRequest;
import br.com.backend.entity.AttendanceRecord;
import br.com.backend.entity.AttendanceSession;
import br.com.backend.entity.Enrollment;
import br.com.backend.entity.TeachingAssignment;
import br.com.backend.entity.enums.AttendanceStatus;
import br.com.backend.exception.BusinessException;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.repository.AttendanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AttendanceServiceTest {

    @Mock
    private TeachingAssignmentService assignmentService;

    @Mock
    private EnrollmentService enrollmentService;

    @Mock
    private AttendanceRepository repository;

    @InjectMocks
    private AttendanceService service;

    private UUID assignmentId;
    private UUID enrollmentId;
    private UUID sessionId;

    private AttendanceCreateRequest createRequest;

    private TeachingAssignment assignment;
    private Enrollment enrollment;
    private AttendanceSession session;

    @BeforeEach
    void setUp() {
        assignmentId = UUID.randomUUID();
        enrollmentId = UUID.randomUUID();
        sessionId = UUID.randomUUID();

        createRequest = new AttendanceCreateRequest(assignmentId, LocalDate.now(), enrollmentId, AttendanceStatus.PRESENT);

        assignment = TeachingAssignmentBuilder.builder().build();
        enrollment = EnrollmentBuilder.builder().build();
        session = AttendanceSessionBuilder.builder()
                .withAssignment(assignment)
                .build();
    }

    @Test
    void shouldRegisterAttendance() {
        when(repository.existsByTeachingAssignmentAndDate(
                createRequest.teachingAssignmentId(), createRequest.date()))
                .thenReturn(false);

        when(assignmentService.findAssignmentById(createRequest.teachingAssignmentId()))
                .thenReturn(assignment);

        when(enrollmentService.findActiveEnrollmentById(createRequest.enrollmentId()))
                .thenReturn(enrollment);

        when(repository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        service.register(createRequest);

        verify(repository).save(any(AttendanceSession.class));
    }

    @Test
    void shouldUpdateAttendance() {
        AttendanceRecord record = AttendanceRecordBuilder.builder()
                .withSession(session)
                .withEnrollment(enrollment)
                .build();

        UUID recordId = UUID.randomUUID();
        ReflectionTestUtils.setField(record, "id", recordId);

        session.getRecords().add(record);

        AttendanceRecordRequest recordRequest =
                new AttendanceRecordRequest(enrollmentId, AttendanceStatus.ABSENT);

        when(repository.findById(sessionId))
                .thenReturn(Optional.of(session));

        service.update(sessionId, recordId, recordRequest);

        verify(repository).findById(sessionId);
        assertEquals(AttendanceStatus.ABSENT, record.getStatus());
    }

    @Test
    void shouldDeleteAttendance() {
        when(repository.findById(sessionId))
                .thenReturn(Optional.of(session));

        service.delete(sessionId);

        verify(repository).findById(sessionId);
        verify(repository).delete(session);
    }

    @Test
    void shouldFindAttendanceSessionById() {
        when(repository.findById(sessionId))
                .thenReturn(Optional.of(session));

        AttendanceSession result = service.findAttendanceSessionById(sessionId);

        verify(repository).findById(sessionId);
        assertEquals(session, result);
    }

    @Test
    void shouldThrowExceptionWhenSessionNotFound() {
        when(repository.findById(sessionId))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> service.findAttendanceSessionById(sessionId));
    }

    @Test
    void shouldThrowExceptionWhenStatusIsNull() {
        AttendanceRecord record = AttendanceRecordBuilder.builder()
                .withSession(session)
                .withEnrollment(enrollment)
                .build();

        UUID recordId = UUID.randomUUID();
        ReflectionTestUtils.setField(record, "id", recordId);

        session.getRecords().add(record);

        AttendanceRecordRequest recordRequest =
                new AttendanceRecordRequest(enrollmentId, null);

        when(repository.findById(sessionId))
                .thenReturn(Optional.of(session));

        assertThrows(
                BusinessException.class,
                () -> service.update(sessionId, recordId, recordRequest));
    }

    @Test
    void shouldThrowExceptionWhenSessionAlreadyExists() {
        when(repository.existsByTeachingAssignmentAndDate(any(), any()))
                .thenReturn(true);

        assertThrows(
                BusinessException.class,
                () -> service.register(createRequest));
    }
}
