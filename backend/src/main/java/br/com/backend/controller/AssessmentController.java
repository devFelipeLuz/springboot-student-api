package br.com.backend.controller;

import br.com.backend.dto.request.AssessmentCreateRequest;
import br.com.backend.dto.request.AssessmentUpdateRequest;
import br.com.backend.dto.response.AssessmentResponseDTO;
import br.com.backend.service.AssessmentService;
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
@RequestMapping("/assessments")
public class AssessmentController {

    private final AssessmentService service;

    public AssessmentController(AssessmentService service) {
        this.service = service;
    }

    @Operation(summary = "Create assessment")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AssessmentResponseDTO register(@Valid @RequestBody AssessmentCreateRequest dto) {
        return service.register(dto);
    }

    @Operation(summary = "List assessments")
    @GetMapping
    public Page<AssessmentResponseDTO> getAssessments(
            @PageableDefault(size = 10, sort = "assessmentDate", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return service.findAll(pageable);
    }

    @Operation(summary = "Find assessment by id")
    @GetMapping("/{id}")
    public AssessmentResponseDTO getAssessmentById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "Update assessment")
    @PatchMapping("/{id}")
    public AssessmentResponseDTO updateAssessment(@PathVariable UUID id,
                                                  @Valid @RequestBody AssessmentUpdateRequest dto) {
        return service.update(id, dto);
    }

    @Operation(summary = "Delete assessment")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
