package br.com.backend.controller;

import br.com.backend.DTO.enrollment.EnrollmentRequestDTO;
import br.com.backend.DTO.enrollment.EnrollmentResponseDTO;
import br.com.backend.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    private EnrollmentService service;

    public EnrollmentController(EnrollmentService service) {
        this.service = service;
    }

    @Operation(summary = "Cria matrícula")
    @PostMapping
    public EnrollmentResponseDTO create(@Valid @RequestBody EnrollmentRequestDTO dto) {
        return service.enroll(dto);
    }

    @Operation(summary = "Busca todas as matriculas e retorna em páginas")
    @GetMapping
    public Page<EnrollmentResponseDTO> findAll(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return service.findAll(pageable);
    }

    @Operation(summary = "Busca todas as matriculas ativas e retorna em páginas")
    @GetMapping("/active")
    public Page<EnrollmentResponseDTO> findAllByStatusActive(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return service.findAllByStatusActive(pageable);
    }

    @Operation(summary = "Busca matrícula por ID")
    @GetMapping("/{id}")
    public EnrollmentResponseDTO findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "Deleta matrícula encontrada por ID")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.cancel(id);
        return ResponseEntity.noContent().build();
    }
}
