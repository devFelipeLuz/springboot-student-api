package br.com.backend.controller;

import br.com.backend.dto.request.ProfessorCreateRequest;
import br.com.backend.dto.request.ProfessorUpdateRequest;
import br.com.backend.dto.response.ProfessorResponseDTO;
import br.com.backend.service.ProfessorService;
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
@RequestMapping("/professors")
public class ProfessorController {

    private final ProfessorService service;

    public ProfessorController(ProfessorService service) {
        this.service = service;
    }

    @Operation(summary = "Create professor")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ProfessorResponseDTO register(@Valid @RequestBody ProfessorCreateRequest dto) {
        return service.register(dto);
    }

    @Operation(summary = "List professors")
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<ProfessorResponseDTO> getProfessors(
            @Parameter(description = "Filter by active status (true or false)")
            @RequestParam Boolean active,

            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return service.findAll(active, pageable);
    }

    @Operation(summary = "Find professor by id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.id")
    public ProfessorResponseDTO getProfessorById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "Update professor")
    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ProfessorResponseDTO updateProfessor(
            @PathVariable UUID id, @Valid @RequestBody ProfessorUpdateRequest dto) {

        return service.update(id, dto);
    }

    @Operation(summary = "Deactivate professor")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deactivateProfessor(@PathVariable UUID id) {
        service.deactivate(id);
    }
}
