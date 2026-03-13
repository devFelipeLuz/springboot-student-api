package br.com.backend.controller;

import br.com.backend.dto.request.StudentGradeUpdateRequest;
import br.com.backend.dto.request.StudentGradeCreateRequest;
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

    @Operation(summary = "Create grade")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public StudentGradeResponseDTO register(@Valid @RequestBody StudentGradeCreateRequest dto) {
        return service.register(dto);
    }

    @Operation(summary = "Find grade by assessment id")
    @GetMapping("/assessment/{assessmentId}/grades")
    public Page<StudentGradeResponseDTO> findGradeByAssessmentId(
            @PathVariable UUID assessmentId,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return service.findGradeByAssessmentId(assessmentId, pageable);
    }

    @Operation(summary = "List grades")
    @GetMapping
    public Page<StudentGradeResponseDTO> getGrades(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return service.findAll(pageable);
    }

    @Operation(summary = "Find grade by id")
    @GetMapping("/{id}")
    public StudentGradeResponseDTO findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "Update grade")
    @PatchMapping("/{id}")
    public StudentGradeResponseDTO update(@PathVariable UUID id,
                                          @Valid @RequestBody StudentGradeUpdateRequest dto) {
        return service.update(id, dto);
    }

    @Operation(summary = "Delete grade")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable UUID id) {
        service.delete(id);
    }
}
