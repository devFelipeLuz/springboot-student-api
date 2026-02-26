package br.com.backend.service;

import br.com.backend.DTO.StudentRequestDTO;
import br.com.backend.DTO.StudentResponseDTO;
import br.com.backend.domain.Student;
import br.com.backend.exception.BusinessException;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.repository.StudentRepository;
import br.com.backend.util.FunctionsUtils;
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

        return FunctionsUtils.toStudentResponseDTO(student);
    }

    public List<StudentResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(FunctionsUtils::toStudentResponseDTO)
                .toList();
    }

    public List<StudentResponseDTO> findAllByActive() {
        return repository.findAllByActiveTrue().stream()
                .map(FunctionsUtils::toStudentResponseDTO)
                .toList();
    }

    public StudentResponseDTO findById(UUID id) {
        Student student = findActiveStudentById(id);
        return FunctionsUtils.toStudentResponseDTO(student);
    }

    public StudentResponseDTO update(UUID id, StudentRequestDTO dto) {
        Student student = findActiveStudentById(id);

        student.saveData(
                dto.getName(),
                dto.getEmail(),
                dto.getAge()
                );

        repository.save(student);
        return FunctionsUtils.toStudentResponseDTO(student);
    }

    public void delete(UUID id) {
        Student student = findActiveStudentById(id);
        student.deactivate();
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
