package br.com.backend.controller;

import br.com.backend.dto.request.EnrollmentRequestDTO;
import br.com.backend.dto.response.EnrollmentResponseDTO;
import br.com.backend.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentService service;

    public EnrollmentController(EnrollmentService service) {
        this.service = service;
    }

    @Operation(summary = "Registra Enrollment")
    @PostMapping
    public EnrollmentResponseDTO register(@Valid @RequestBody EnrollmentRequestDTO dto) {
        return service.enroll(dto);
    }

    @Operation(summary = "Busca Enrollment e retorna em páginas")
    @GetMapping
    public Page<EnrollmentResponseDTO> getEnrollments(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return service.findAll(pageable);
    }

    @Operation(summary = "Busca Enrollments ativas e retorna em páginas")
    @GetMapping("/active")
    public Page<EnrollmentResponseDTO> getActiveEnrollments(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return service.findAllByStatusActive(pageable);
    }

    @Operation(summary = "Busca Enrollment por ID")
    @GetMapping("/{id}")
    public EnrollmentResponseDTO findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "Deleta Enrollment encontrada por ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.cancel(id);
    }
}
