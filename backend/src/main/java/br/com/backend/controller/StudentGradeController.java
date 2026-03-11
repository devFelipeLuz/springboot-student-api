package br.com.backend.controller;

import br.com.backend.dto.request.GradeUpdateDTO;
import br.com.backend.dto.request.StudentGradeRequestDTO;
import br.com.backend.dto.response.StudentGradeResponseDTO;
import br.com.backend.service.StudentGradeService;
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
@RequestMapping("/grades")
public class StudentGradeController {

    private final StudentGradeService service;

    public StudentGradeController(StudentGradeService service) {
        this.service = service;
    }

    @Operation(summary = "Registra Grade")
    @PostMapping
    public StudentGradeResponseDTO register(@Valid @RequestBody StudentGradeRequestDTO dto) {
        return service.register(dto);
    }

    @Operation(summary = "Busca Grade por AssessmentId")
    @GetMapping("/assessment/{assessmentId}")
    public Page<StudentGradeResponseDTO> findByAssessmentId(
            @PathVariable UUID assessmentId,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return service.findByAssessmentId(assessmentId, pageable);
    }

    @Operation(summary = "Busca Grade e retorna em páginas")
    @GetMapping
    public Page<StudentGradeResponseDTO> getGrades(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return service.findAll(pageable);
    }

    @Operation(summary = "Busca Grade por ID")
    @GetMapping("/{id}")
    public StudentGradeResponseDTO findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "Atualiza Grade encontrado por ID")
    @PatchMapping("/{id}")
    public StudentGradeResponseDTO update(@PathVariable UUID id,
                                          @Valid @RequestBody GradeUpdateDTO dto) {
        return service.update(id, dto);
    }

    @Operation(summary = "Deleta Grade encontrado por ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable UUID id) {
        service.delete(id);
    }
}
