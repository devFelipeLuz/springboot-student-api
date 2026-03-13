package br.com.backend.controller;

import br.com.backend.dto.request.StudentCreateRequest;
import br.com.backend.dto.request.StudentUpdateRequest;
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

    @Operation(summary = "Create student")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public StudentResponseDTO register(@Valid @RequestBody StudentCreateRequest dto) {
        return service.register(dto);
    }

    @Operation(summary = "Find student by id")
    @GetMapping("/{id}")
    public StudentResponseDTO findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "List students")
    @GetMapping
    public Page<StudentResponseDTO> findAll(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return service.findAll(pageable);
    }

    @Operation(summary = "List active students")
    @GetMapping("/active")
    public Page<StudentResponseDTO> findAllByActive(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return service.findAllActive(pageable);
    }

    @Operation(summary = "Update student")
    @PatchMapping("/{id}")
    public StudentResponseDTO update(@PathVariable UUID id,
                                     @Valid @RequestBody StudentUpdateRequest dto) {
        return service.update(id, dto);
    }

    @Operation(summary = "Deactivate student")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deactivateStudent(@PathVariable UUID id) {
        service.deactivate(id);
    }
}
