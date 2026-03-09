package br.com.backend.controller;

import br.com.backend.DTO.grade.GradeRequestDTO;
import br.com.backend.DTO.grade.GradeResponseDTO;
import br.com.backend.service.GradeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/grades")
public class GradeController {

    private final GradeService service;

    public GradeController(GradeService service) {
        this.service = service;
    }

    @Operation(summary = "Registra uma Grade")
    @PostMapping
    public GradeResponseDTO register(@Valid @RequestBody GradeRequestDTO dto) {
        return service.create(dto);
    }

    @Operation(summary = "Busca todas as Grades e retorna em páginas")
    @GetMapping
    public Page<GradeResponseDTO> findAll(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return service.findAll(pageable);
    }

    @Operation(summary = "Busca uma Grade por ID")
    @GetMapping("/{id}")
    public GradeResponseDTO findById(@PathVariable UUID id) {
        return service.findById(id);
    }
}
