package br.com.backend.unit.service;

import br.com.backend.builders.ClassroomBuilder;
import br.com.backend.builders.SchoolYearBuilder;
import br.com.backend.dto.request.ClassroomChangeCapacityRequest;
import br.com.backend.dto.request.ClassroomCreateRequest;
import br.com.backend.entity.Classroom;
import br.com.backend.entity.SchoolYear;
import br.com.backend.exception.BusinessException;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.repository.ClassroomRepository;
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
public class ClassroomServiceTest {

    @Mock
    private SchoolYearService schoolYearService;

    @Mock
    private ClassroomRepository repository;

    @InjectMocks
    private ClassroomService service;

    private UUID schoolYearId;
    private UUID classroomId;

    private SchoolYear schoolYear;
    private Classroom classroom;

    private ClassroomCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        schoolYearId = UUID.randomUUID();
        classroomId = UUID.randomUUID();

        schoolYear = SchoolYearBuilder.builder().build();
        createRequest = new ClassroomCreateRequest("3.A", schoolYearId);
        classroom = ClassroomBuilder.builder().build();
    }

    @Test
    void shouldRegisterClassroom() {
        when(schoolYearService.findActiveSchoolYear(createRequest.schoolYearId()))
                .thenReturn(schoolYear);

        when(repository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        service.register(createRequest);

        verify(repository).save(any(Classroom.class));
    }

    @Test
    void shouldChangeClassroomCapacity() {
        ClassroomChangeCapacityRequest updateRequest = new ClassroomChangeCapacityRequest(30);

        when(repository.findById(classroomId)).thenReturn(Optional.of(classroom));

        service.changeCapacity(classroomId, updateRequest);

        assertEquals(30, classroom.getMaxCapacity());
        verify(repository).findById(classroomId);
    }

    @Test
    void shouldDeactivateClassroom() {
        when(repository.findById(classroomId)).thenReturn(Optional.of(classroom));

        service.deactivate(classroomId);

        assertFalse(classroom.isActive());
    }

    @Test
    void shouldFindActiveClassroomById() {
        when(repository.findById(classroomId)).thenReturn(Optional.of(classroom));

        Classroom result = service.findActiveClassroomById(classroomId);

        assertEquals(classroom, result);
    }

    @Test
    void shouldThrowExceptionWhenClassroomIsNotFound() {
        when(repository.findById(classroomId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, ()-> service.deactivate(classroomId));
    }

    @Test
    void shouldThrowExceptionWhenClassroomIsFull() {
        ClassroomChangeCapacityRequest updateRequest = new ClassroomChangeCapacityRequest(0);

        when(repository.findById(classroomId)).thenReturn(Optional.of(classroom));

        classroom.increaseActiveEnrollmentsCount();

        assertThrows(BusinessException.class, ()-> service.changeCapacity(classroomId, updateRequest));
    }

    @Test
    void shouldThrowExceptionWhenClassroomIsInactive() {
        classroom.deactivate();

        when(repository.findById(classroomId)).thenReturn(Optional.of(classroom));

        assertThrows(BusinessException.class, ()-> service.findActiveClassroomById(classroomId));
    }
}
