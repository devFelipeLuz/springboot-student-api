package br.com.backend.controller;

import br.com.backend.dto.request.EnrollmentRequest;
import br.com.backend.dto.response.EnrollmentResponseDTO;
import br.com.backend.entity.enums.EnrollmentStatus;
import br.com.backend.service.EnrollmentService;
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
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentService service;

    public EnrollmentController(EnrollmentService service) {
        this.service = service;
    }

    @Operation(summary = "Create enrollment",
    description = "Enrolls a student in a classroom for a given school year")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public EnrollmentResponseDTO register(@Valid @RequestBody EnrollmentRequest dto) {
        return service.enroll(dto);
    }

    @Operation(summary = "Find enrollment by id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public EnrollmentResponseDTO getEnrollmentById(
            @PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "List enrollments")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public Page<EnrollmentResponseDTO> getEnrollments(
            @Parameter(description = "Filter by partial or full studentName")
            @RequestParam(required = false)
            String studentName,

            @Parameter(description = "Filter by enrollment status (ACTIVE, CANCELED or FINISHED)")
            @RequestParam(required = false)
            EnrollmentStatus status,

            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return service.findAll(studentName, status, pageable);
    }

    @Operation(summary = "Finish enrollment")
    @PatchMapping("/{id}/finish")
    @PreAuthorize("hasAuthority('ADMIN')")
    public EnrollmentResponseDTO finishEnrollment(@PathVariable UUID id) {
        return service.finishEnrollment(id);
    }

    @Operation(summary = "Cancel enrollment")
    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('ADMIN')")
    public EnrollmentResponseDTO cancelEnrollment(@PathVariable UUID id) {
        return service.cancel(id);
    }
}
