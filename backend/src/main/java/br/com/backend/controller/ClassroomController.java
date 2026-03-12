package br.com.backend.controller;

import br.com.backend.dto.request.ClassroomChangeCapacityRequest;
import br.com.backend.dto.request.ClassroomRequestDTO;
import br.com.backend.dto.response.ClassroomResponseDTO;
import br.com.backend.service.ClassroomService;
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
@RequestMapping("/classrooms")
public class ClassroomController {

    private final ClassroomService service;

    public ClassroomController(ClassroomService service) {
        this.service = service;
    }

    @Operation(summary = "Create classroom")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ClassroomResponseDTO register(@Valid @RequestBody ClassroomRequestDTO dto) {
        return service.register(dto);
    }

    @Operation(summary = "List classrooms")
    @GetMapping
    public Page<ClassroomResponseDTO> findAll(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return service.findAll(pageable);
    }

    @Operation(summary = "Find classroom by id")
    @GetMapping("/{id}")
    public ClassroomResponseDTO findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "Change classroom capacity")
    @PatchMapping("/{id}")
    public ClassroomResponseDTO changeCapacity(@PathVariable UUID id,
                                              @Valid @RequestBody ClassroomChangeCapacityRequest dto) {
        return service.changeCapacity(id, dto);
    }

    @Operation(summary = "Deactivate classroom")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deactivate(@PathVariable UUID id) {
        service.deactivateClassroom(id);
    }
}
