package br.com.backend.controller;

import br.com.backend.dto.request.StudentGradeFilter;
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
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public StudentGradeResponseDTO registerGrade(@Valid @RequestBody StudentGradeCreateRequest dto) {
        return service.register(dto);
    }

    @Operation(summary = "Find grade by assessment id")
    @GetMapping("/assessment/{assessmentId}/grades")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<StudentGradeResponseDTO> findGradeByAssessmentId(
            @PathVariable UUID assessmentId,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return service.findGradeByAssessmentId(assessmentId, pageable);
    }

    @Operation(summary = "Find grade by id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR', 'STUDENT')")
    public StudentGradeResponseDTO findGradeById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "List grades")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public Page<StudentGradeResponseDTO> getGrades(
            @RequestParam(required = false)
            StudentGradeFilter filter,

            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {

        return service.findAll(filter, pageable);
    }

    @Operation(summary = "Update grade")
    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public StudentGradeResponseDTO updateGrade(
            @PathVariable UUID id, @Valid @RequestBody StudentGradeUpdateRequest dto) {
        return service.update(id, dto);
    }

    @Operation(summary = "Delete grade")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteById(@PathVariable UUID id) {
        service.delete(id);
    }
}
