package com.example.backend.service;

import com.example.backend.DTO.StudentRequestDTO;
import com.example.backend.DTO.StudentResponseDTO;
import com.example.backend.entity.Student;
import com.example.backend.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setAge(dto.getAge());
        student.setGrade(dto.getGrade());

        Student saved = repository.save(student);


        return toResponseDTO(saved);
    }

    public List<StudentResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public Student findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Aluno não encontrado"
                ));
    }

    public StudentResponseDTO update(UUID id, StudentRequestDTO dto) {
        Student existing = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Aluno não encontrado"
                ));

        existing.setName(dto.getName());
        existing.setAge(dto.getAge());
        existing.setGrade(dto.getGrade());

        Student updated = repository.save(existing);

        return toResponseDTO(updated);
    }

    public void delete(UUID id) {
        if(!repository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Aluno não encontrado"
            );
        }
        repository.deleteById(id);
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
