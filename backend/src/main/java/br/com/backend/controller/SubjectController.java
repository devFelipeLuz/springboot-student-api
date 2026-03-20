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
import org.springframework.data.repository.query.Param;
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
    @PreAuthorize("hasAuthority('ADMIN', 'PROFESSOR')")
    public SubjectResponseDTO register(@Valid @RequestBody SubjectRequest dto) {
        return service.register(dto);
    }

    @Operation(summary = "Find subject by id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN', 'PROFESSOR')")
    public SubjectResponseDTO findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "List subjects")
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN', 'PROFESSOR')")
    public Page<SubjectResponseDTO> getSubjects(
            @Parameter(description = "Filter by status active (true or false)")
            @RequestParam
            Boolean active,

            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return service.findAll(active, pageable);
    }

    @Operation(summary = "Update subject")
    @PatchMapping("/{id}")
    public SubjectResponseDTO update(@PathVariable UUID id,
                                     @Valid @RequestBody SubjectRequest dto) {
        return service.updateName(id, dto);
    }

    @Operation(summary = "Deactivate subject")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deactivateSubject(@PathVariable UUID id) {
        service.deactivate(id);
    }
}
