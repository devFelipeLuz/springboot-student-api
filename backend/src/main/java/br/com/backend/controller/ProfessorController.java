package br.com.backend.controller;

import br.com.backend.dto.request.ProfessorCreateRequest;
import br.com.backend.dto.request.ProfessorUpdateRequest;
import br.com.backend.dto.response.ProfessorResponseDTO;
import br.com.backend.service.ProfessorService;
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
@RequestMapping("/professors")
public class ProfessorController {

    private ProfessorService service;

    public ProfessorController(ProfessorService service) {
        this.service = service;
    }

    @Operation(summary = "Create professor")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ProfessorResponseDTO register(@Valid @RequestBody ProfessorCreateRequest dto) {
        return service.register(dto);
    }

    @Operation(summary = "List professors")
    @GetMapping
    public Page<ProfessorResponseDTO> getProfessors(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return service.findAll(pageable);
    }

    @Operation(summary = "Find professor by id")
    @GetMapping("/{id}")
    public ProfessorResponseDTO getProfessorById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "Update professor")
    @PatchMapping("/{id}")
    public ProfessorResponseDTO update(@PathVariable UUID id,
                                       @Valid @RequestBody ProfessorUpdateRequest dto) {
        return service.update(id, dto);
    }

    @Operation(summary = "Deactivate professor")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deactivateProfessor(@PathVariable UUID id) {
        service.deactivate(id);
    }
}
