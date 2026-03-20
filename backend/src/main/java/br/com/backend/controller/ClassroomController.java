package br.com.backend.controller;

import br.com.backend.dto.request.ClassroomChangeCapacityRequest;
import br.com.backend.dto.request.ClassroomCreateRequest;
import br.com.backend.dto.response.ClassroomResponseDTO;
import br.com.backend.service.ClassroomService;
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
@RequestMapping("/classrooms")
public class ClassroomController {

    private final ClassroomService service;

    public ClassroomController(ClassroomService service) {
        this.service = service;
    }

    @Operation(summary = "Create classroom")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public ClassroomResponseDTO registerClassroom(@Valid @RequestBody ClassroomCreateRequest dto) {
        return service.register(dto);
    }

    @Operation(summary = "List classrooms")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public Page<ClassroomResponseDTO> getClassrooms(
            @Parameter(description = "Filter by active status (true or false)")
            @RequestParam(required = false)
            Boolean active,

            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return service.findAll(active, pageable);
    }

    @Operation(summary = "Find classroom by id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public ClassroomResponseDTO getClassroomById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "Change classroom capacity")
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public ClassroomResponseDTO changeClassroomCapacity(
            @PathVariable UUID id,
            @Valid @RequestBody ClassroomChangeCapacityRequest dto) {

        return service.changeCapacity(id, dto);
    }

    @Operation(summary = "Deactivate classroom")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public void deactivateClassroom(@PathVariable UUID id) {
        service.deactivate(id);
    }
}
