package br.com.backend.controller;

import br.com.backend.dto.request.AssessmentCreateRequest;
import br.com.backend.dto.request.AssessmentUpdateRequest;
import br.com.backend.dto.response.AssessmentResponseDTO;
import br.com.backend.entity.enums.AssessmentType;
import br.com.backend.service.AssessmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/assessments")
public class AssessmentController {

    private final AssessmentService service;

    public AssessmentController(AssessmentService service) {
        this.service = service;
    }

    @Operation(summary = "Create assessment")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public AssessmentResponseDTO registerAssessment(@Valid @RequestBody AssessmentCreateRequest dto) {
        return service.register(dto);
    }

    @Operation(summary = "Find assessment by id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public AssessmentResponseDTO getAssessmentById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "List assessments")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public Page<AssessmentResponseDTO> getAssessments(
            @Parameter(description = "Filter by partial or full title")
            @RequestParam(required = false)
            String title,

            @Parameter(description = "Filter by assessment type (PROVA, TRABALHO, LIÇÃO)")
            @RequestParam(required = false)
            AssessmentType type,

            @PageableDefault(size = 10, sort = "assessmentDate", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return service.findAll(title, type, pageable);
    }

    @Operation(summary = "Update assessment")
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public AssessmentResponseDTO updateAssessment(
            @PathVariable UUID id, @Valid @RequestBody AssessmentUpdateRequest dto) {
        return service.update(id, dto);
    }

    @Operation(summary = "Delete assessment")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public void deleteAssessment(@PathVariable UUID id) {
        service.delete(id);
    }
}
