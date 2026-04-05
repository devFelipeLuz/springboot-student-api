package br.com.backend.controller;

import br.com.backend.dto.request.SubjectRequest;
import br.com.backend.dto.response.SubjectResponseDTO;
import br.com.backend.service.SubjectService;
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
@RequestMapping("/subjects")
public class SubjectController {

    private final SubjectService service;

    public SubjectController(SubjectService service){
        this.service = service;
    }

    @Operation(summary = "Create subject")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public SubjectResponseDTO registerSubject(@Valid @RequestBody SubjectRequest dto) {
        return service.register(dto);
    }

    @Operation(summary = "Find subject by id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public SubjectResponseDTO findSubjectById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "List subjects")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public Page<SubjectResponseDTO> getSubjects(
            @Parameter(description = "Filter by partial or full subject name")
            @RequestParam(required = false)
            String name,

            @Parameter(description = "Filter by status active (true or false)")
            @RequestParam(required = false)
            Boolean active,

            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return service.findAll(name, active, pageable);
    }

    @Operation(summary = "Update subject")
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public SubjectResponseDTO updateSubject(@PathVariable UUID id,
                                     @Valid @RequestBody SubjectRequest dto) {
        return service.updateName(id, dto);
    }

    @Operation(summary = "Deactivate subject")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deactivateSubject(@PathVariable UUID id) {
        service.deactivate(id);
    }
}
