package br.com.backend.controller;

import br.com.backend.dto.request.TeachingAssignmentRequest;
import br.com.backend.dto.response.TeachingAssignmentResponseDTO;
import br.com.backend.service.TeachingAssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/assignments")
public class TeachingAssignmentController {

    private final TeachingAssignmentService service;

    public TeachingAssignmentController(TeachingAssignmentService service) {
        this.service = service;
    }

    @Operation(summary = "Create assignment")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public TeachingAssignmentResponseDTO registerAssignment(@Valid @RequestBody TeachingAssignmentRequest dto) {
        return service.register(dto);
    }

    @Operation(summary = "List assignments")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public Page<TeachingAssignmentResponseDTO> getAssignments(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return service.findAll(pageable);
    }

    @Operation(summary = "Find assignment by id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public TeachingAssignmentResponseDTO getAssignmentById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "Delete assignment")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public void deleteAssignmentById(@PathVariable UUID id) {
        service.delete(id);
    }
}
