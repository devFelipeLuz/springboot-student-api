package br.com.backend.controller;

import br.com.backend.dto.request.SchoolYearRequest;
import br.com.backend.dto.response.SchoolYearResponseDTO;
import br.com.backend.service.SchoolYearService;
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
@RequestMapping("/school-years")
public class SchoolYearController {

    private final SchoolYearService service;

    public SchoolYearController(SchoolYearService service) {
        this.service = service;
    }

    @Operation(summary = "Create schoolYear")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public SchoolYearResponseDTO registerSchoolYear(@Valid @RequestBody SchoolYearRequest dto) {
        return service.register(dto);
    }

    @Operation(summary = "Find schoolYear by id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public SchoolYearResponseDTO getSchoolYearById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "List schoolYears")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public Page<SchoolYearResponseDTO> getSchoolYears(
            @Parameter(description = "Filter by partial or full school year")
            Integer year,

            @Parameter(description = "Filter by status active (true or false)")
            @RequestParam(required = false)
            Boolean active,

            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return service.findAll(year, active, pageable);
    }

    @Operation(summary = "Update schoolYear")
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public SchoolYearResponseDTO updateSchoolYear(@PathVariable UUID id,
                                        @Valid @RequestBody SchoolYearRequest dto) {
        return service.update(id, dto);
    }

    @Operation(summary = "Deactivate schoolYear")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deactivateSchoolYear(@PathVariable UUID id) {
        service.deactivate(id);
    }
}
