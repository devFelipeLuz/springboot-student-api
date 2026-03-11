package br.com.backend.controller;

import br.com.backend.dto.request.StudentRequestDTO;
import br.com.backend.dto.response.StudentResponseDTO;
import br.com.backend.service.StudentService;
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
@RequestMapping("/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @Operation(summary = "Registra Student")
    @PostMapping
    public StudentResponseDTO register(@Valid @RequestBody StudentRequestDTO dto) {
        return service.register(dto);
    }

    @Operation(summary = "Busca Student por ID")
    @GetMapping("/{id}")
    public StudentResponseDTO findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "Busca Students e retorna em páginas")
    @GetMapping
    public Page<StudentResponseDTO> findAll(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return service.findAll(pageable);
    }

    @Operation(summary = "Busca Students ativos e retorna em páginas")
    @GetMapping("/active")
    public Page<StudentResponseDTO> findAllByActive(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return service.findAllByActive(pageable);
    }

    @Operation(summary = "Atualiza Student encontrado por ID")
    @PutMapping("/{id}")
    public StudentResponseDTO update(@PathVariable UUID id,
                          @Valid @RequestBody StudentRequestDTO dto) {
        return service.update(id, dto);
    }

    @Operation(summary = "Deleta Student encotrado por ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
