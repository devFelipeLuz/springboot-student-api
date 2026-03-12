package br.com.backend.controller;

import br.com.backend.dto.request.AssessmentCreateRequest;
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
    public AssessmentResponseDTO register(@Valid @RequestBody AssessmentCreateRequest request) {
        return service.register(request);
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
    public AssessmentResponseDTO findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "Update assessment")
    @PatchMapping("/{id}")
    public AssessmentResponseDTO update(@PathVariable UUID id,
                                        @Valid @RequestBody AssessmentCreateRequest request
    ) {
        return service.update(id, request);
    }

    @Operation(summary = "Delete assessment")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
