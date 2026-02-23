package br.com.backend.controller;

import br.com.backend.DTO.StudentRequestDTO;
import br.com.backend.DTO.StudentResponseDTO;
import br.com.backend.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @PostMapping
    public StudentResponseDTO register(@Valid @RequestBody StudentRequestDTO dto) {
        return service.create(dto);
    }

    @GetMapping("/{id}")
    public StudentResponseDTO findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @GetMapping
    public List<StudentResponseDTO> findAll() {
        return service.findAll();
    }

    @PutMapping("/{id}")
    public StudentResponseDTO update(@PathVariable UUID id,
                          @Valid @RequestBody StudentRequestDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
