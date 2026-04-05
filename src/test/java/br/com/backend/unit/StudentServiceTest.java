package br.com.backend.unit;

import br.com.backend.builders.entity.StudentBuilder;
import br.com.backend.builders.entity.UserBuilder;
import br.com.backend.dto.request.StudentCreateRequest;
import br.com.backend.dto.request.StudentUpdateRequest;
import br.com.backend.entity.Student;
import br.com.backend.entity.User;
import br.com.backend.entity.enums.Role;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.repository.StudentRepository;
import br.com.backend.service.StudentService;
import br.com.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private StudentRepository repository;

    @InjectMocks
    private StudentService service;

    private User user;
    private User newUser;

    private UUID studentId;

    private Student student;

    private StudentCreateRequest createRequest;
    private StudentUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {

        user = UserBuilder.builder().build();
        newUser = UserBuilder.builder()
                .withEmail("ezio.auditore@teste.com")
                .build();

        studentId = UUID.randomUUID();

        student = StudentBuilder.builder().build();

        createRequest = new StudentCreateRequest(
                "Student Test", "student@test.com", "studentPassword");

        updateRequest = new StudentUpdateRequest(
                "Ezio Auditore da Firenze", "ezio.auditore@teste.com", null);
    }

    @Test
    void shouldRegisterStudent(){
        when(userService.registerUser(createRequest.email(), createRequest.password(), Role.STUDENT))
                .thenReturn(user);
        when(repository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        service.register(createRequest);

        verify(repository).save(any(Student.class));
    }

    @Test
    void shouldFindActiveStudentById() {
        when(repository.findById(studentId)).thenReturn(Optional.of(student));
        Student result = service.findActiveStudentById(studentId);
        assertEquals(student, result);
    }

    @Test
    void shouldUpdateStudent() {
        when(repository.findById(studentId)).thenReturn(Optional.of(student));
        when(userService.changeEmail(student.getUser().getId(), updateRequest.email()))
                .thenReturn(newUser);

        service.update(studentId, updateRequest);

        assertEquals("Ezio Auditore da Firenze", student.getName());
    }

    @Test
    void shouldDeactivateStudent() {
        when(repository.findById(studentId)).thenReturn(Optional.of(student));
        service.deactivate(studentId);
        assertFalse(student.isActive());
    }

    @Test
    void shouldThrowExceptionWhenStudentNotFound() {
        when(repository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.deactivate(studentId));
    }
}
