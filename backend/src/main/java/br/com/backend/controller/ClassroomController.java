package br.com.backend.controller;

import br.com.backend.dto.request.ClassroomRequestDTO;
import br.com.backend.dto.response.ClassroomResponseDTO;
import br.com.backend.service.ClassroomService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/classrooms")
public class ClassroomController {

    private final ClassroomService service;

    public ClassroomController(ClassroomService service) {
        this.service = service;
    }

    @Operation(summary = "Registra Classroom")
    @PostMapping
    public ClassroomResponseDTO register(@Valid @RequestBody ClassroomRequestDTO dto) {
        return service.register(dto);
    }

    @Operation(summary = "Busca Classroom e retorna em páginas")
    @GetMapping
    public Page<ClassroomResponseDTO> findAll(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return service.findAll(pageable);
    }

    @Operation(summary = "Busca Classroom por ID")
    @GetMapping("/{id}")
    public ClassroomResponseDTO findById(@PathVariable UUID id) {
        return service.findById(id);
    }
}
