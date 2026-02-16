package com.example.backend.service;

import com.example.backend.DTO.StudentRequestDTO;
import com.example.backend.DTO.StudentResponseDTO;
import com.example.backend.entity.Student;
import com.example.backend.exception.BusinessException;
import com.example.backend.exception.StudentNotFoundException;
import com.example.backend.repository.StudentRepository;
import org.springframework.http.HttpStatus;
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
        Student student = new Student();
        student.updateData(
                dto.getName(),
                dto.getEmail(),
                dto.getAge(),
                dto.getGrade()
        );

        repository.save(student);

        return toResponseDTO(student);
    }

    public List<StudentResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .filter(s -> s.getActive().equals(true))
                .map(this::toResponseDTO)
                .toList();
    }

    public Student findById(UUID id) {
        Student student = repository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(
                        HttpStatus.NOT_FOUND, "Aluno não encontrado"
                ));

        if (!student.getActive()) {
            throw new BusinessException("Aluno inativo");
        }

        return student;
    }

    public StudentResponseDTO update(UUID id, StudentRequestDTO dto) {
        Student student = repository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(
                        HttpStatus.NOT_FOUND, "Aluno não encontrado"
                ));

        student.updateData(
                dto.getName(),
                dto.getEmail(),
                dto.getAge(),
                dto.getGrade());

        repository.save(student);

        return toResponseDTO(student);
    }

    public void delete(UUID id) {
        Student student = repository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(
                        HttpStatus.NOT_FOUND, "Aluno não encontrado"
                ));

        student.deactivate();
    }

    private StudentResponseDTO toResponseDTO (Student student) {
        return new StudentResponseDTO(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getAge(),
                student.getGrade()
        );
    }
}
