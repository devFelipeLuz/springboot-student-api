package br.com.backend.controller;

import br.com.backend.dto.request.ProfessorRequestDTO;
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

    @Operation(summary = "Registra Professor")
    @PostMapping
    public ProfessorResponseDTO register(@Valid @RequestBody ProfessorRequestDTO dto) {
        return service.register(dto);
    }

    @Operation(summary = "Busca Professor e retorna em páginas")
    @GetMapping
    public Page<ProfessorResponseDTO> getProfessors(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return service.findAll(pageable);
    }

    @Operation(summary = "Busca Professor por ID")
    @GetMapping("/{id}")
    public ProfessorResponseDTO findById(@PathVariable UUID professorId) {
        return service.findById(professorId);
    }

    @Operation(summary = "Atualiza Professor encontrado por ID")
    @PatchMapping("/{id}")
    public ProfessorResponseDTO update(@PathVariable UUID professorId,
                                                @Valid @RequestBody ProfessorRequestDTO dto) {
        return service.update(professorId, dto);
    }

    @Operation(summary = "Desativa Professor encontrado por ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteProfessor(@PathVariable UUID professorId) {
        service.delete(professorId);
    }
}
