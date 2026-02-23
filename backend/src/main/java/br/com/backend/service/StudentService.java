package br.com.backend.service;

import br.com.backend.DTO.StudentRequestDTO;
import br.com.backend.DTO.StudentResponseDTO;
import br.com.backend.domain.Student;
import br.com.backend.exception.BusinessException;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public StudentResponseDTO create(StudentRequestDTO dto) {
        Student student = new Student(
                dto.getName(),
                dto.getEmail(),
                dto.getAge());

        repository.save(student);

        return toResponseDTO(student);
    }

    public List<StudentResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<StudentResponseDTO> findAllByActive() {
        return repository.findAllByActiveTrue().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public StudentResponseDTO findById(UUID id) {
        Student student = findActiveStudentById(id);
        return toResponseDTO(student);
    }

    public StudentResponseDTO update(UUID id, StudentRequestDTO dto) {
        Student student = findActiveStudentById(id);

        student.saveData(
                dto.getName(),
                dto.getEmail(),
                dto.getAge()
                );

        repository.save(student);
        return toResponseDTO(student);
    }

    public void delete(UUID id) {
        Student student = findActiveStudentById(id);
        student.deactivate();
    }

    private StudentResponseDTO toResponseDTO (Student student) {
        String gradeName = student.getActiveEnrollments()
                .map(e -> e.getGrade().getName())
                .orElse(null);

        return new StudentResponseDTO(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getAge(),
                gradeName
        );
    }

    public Student findActiveStudentById(UUID id) {
        Student student = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));

        if (!student.getActive()) {
            throw new BusinessException("Aluno inativo");
        }

        return student;
    }
}
