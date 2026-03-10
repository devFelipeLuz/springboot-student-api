package br.com.backend.controller;

import br.com.backend.DTO.assessment.AssessmentRequestDTO;
import br.com.backend.DTO.assessment.AssessmentResponseDTO;
import br.com.backend.service.AssessmentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/assessments")
public class AssessmentController {

    private final AssessmentService service;

    public AssessmentController(AssessmentService service) {
        this.service = service;
    }

    @Operation(summary = "Registra assessment")
    @PostMapping
    public AssessmentResponseDTO register(@RequestBody AssessmentRequestDTO request) {
        return service.register(request);
    }

    @Operation(summary = "Busca todas assessments e retorna em páginas")
    @GetMapping
    public Page<AssessmentResponseDTO> getAssessments(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return service.findAll(pageable);
    }

    @Operation(summary = "Busca assessment por ID")
    @GetMapping("/{id}")
    public AssessmentResponseDTO findByID(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "Atualiza assessment encontrado por ID")
    @PutMapping("/{id}")
    public AssessmentResponseDTO update(
            @PathVariable UUID id,
            @RequestBody AssessmentRequestDTO request
    ) {
        return service.update(id, request);
    }

    @Operation(summary = "Deleta assessment por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
