package br.com.backend.service;

import br.com.backend.dto.request.StudentCreateRequest;
import br.com.backend.dto.request.StudentUpdateRequest;
import br.com.backend.dto.response.StudentResponseDTO;
import br.com.backend.entity.Student;
import br.com.backend.entity.User;
import br.com.backend.entity.enums.Role;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.mapper.StudentMapper;
import br.com.backend.repository.StudentRepository;
import br.com.backend.repository.UserRepository;
import br.com.backend.specification.StudentSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Service
@Transactional
public class StudentService {

    private final StudentRepository repository;
    private final UserService userService;

    public StudentService(StudentRepository repository,
                          UserRepository userRepository,
                          UserService userService) {

        this.repository = repository;
        this.userService = userService;
    }

    public StudentResponseDTO register(StudentCreateRequest dto) {
        User user = userService.registerUser(dto.email(), dto.password(), Role.STUDENT);

        Student student = new Student(dto.name(), user);
        Student saved = repository.save(student);

        return StudentMapper.toDTO(saved);
    }

    public StudentResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(StudentMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
    }

    public Page<StudentResponseDTO> findAll(String name, String email, Boolean active, Pageable pageable) {
        Specification<Student> spec =
                StudentSpecification.withFilters(name, email, active);

        return repository.findAll(spec, pageable)
                .map(StudentMapper::toDTO);
    }

    public StudentResponseDTO update(UUID id, StudentUpdateRequest dto) {
        Student student = findActiveStudentById(id);

        if (dto.name() != null) {
            student.updateName(dto.name());
        }

        if (dto.email() != null) {
            userService.changeEmail(student.getUser().getId(), dto.email());
        }

        if (dto.password() != null) {
            userService.changePassword(student.getUser().getId(), dto.password());
        }

        return StudentMapper.toDTO(student);
    }

    public void deactivate(UUID id) {
        Student student = findActiveStudentById(id);
        student.deactivate();
    }

    public Student findActiveStudentById(UUID id) {
        Student student = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        student.ensureActive();
        return student;
    }

    public boolean isOwner(UUID studentId, UUID userId) {
        Student student = repository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        return student.getUser().getId().equals(userId);
    }
}
