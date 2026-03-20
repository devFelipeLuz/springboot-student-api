package br.com.backend.controller;

import br.com.backend.dto.request.StudentCreateRequest;
import br.com.backend.dto.request.StudentUpdateRequest;
import br.com.backend.dto.response.StudentResponseDTO;
import br.com.backend.service.StudentService;
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
@RequestMapping("/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @Operation(summary = "Create student")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public StudentResponseDTO registerStudent(@Valid @RequestBody StudentCreateRequest dto) {
        return service.register(dto);
    }

    @Operation(summary = "Find student by id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.id")
    public StudentResponseDTO getStudentById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "List students")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public Page<StudentResponseDTO> getStudents(
            @Parameter(description = "Filter by active status (true or false)")
            @RequestParam(required = false)
            Boolean active,

            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.DESC)
            Pageable pageable) {

        return service.findAll(active, pageable);
    }

    @Operation(summary = "Update student")
    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.id")
    public StudentResponseDTO updateStudent(@PathVariable UUID id,
                                     @Valid @RequestBody StudentUpdateRequest dto) {
        return service.update(id, dto);
    }

    @Operation(summary = "Deactivate student")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deactivateStudent(@PathVariable UUID id) {
        service.deactivate(id);
    }
}
