package br.com.backend.controller;

import br.com.backend.DTO.EnrollmentRequestDTO;
import br.com.backend.DTO.EnrollmentResponseDTO;
import br.com.backend.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    private EnrollmentService service;

    public EnrollmentController(EnrollmentService service) {
        this.service = service;
    }

    @PostMapping
    public EnrollmentResponseDTO create(@Valid @RequestBody EnrollmentRequestDTO dto) {
        return service.enroll(dto);
    }

    @GetMapping
    public List<EnrollmentResponseDTO> findAll() {
        return service.findAll();
    }

    @GetMapping
    public List<EnrollmentResponseDTO> findAllByStatusActive() {
        return service.findAllByStatusActive();
    }

    @GetMapping("/{id}")
    public EnrollmentResponseDTO findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.cancel(id);
        return ResponseEntity.noContent().build();
    }
}
