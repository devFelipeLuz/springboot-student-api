package br.com.backend.controller;

import br.com.backend.dto.request.EnrollmentRequest;
import br.com.backend.dto.response.EnrollmentResponseDTO;
import br.com.backend.service.EnrollmentService;
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
    public EnrollmentResponseDTO register(@Valid @RequestBody EnrollmentRequest dto) {
        return service.enroll(dto);
    }

    @Operation(summary = "List enrollments")
    @GetMapping
    public Page<EnrollmentResponseDTO> getEnrollments(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return service.findAll(pageable);
    }

    @Operation(summary = "Find active enrollments")
    @GetMapping("/active")
    public Page<EnrollmentResponseDTO> getActiveEnrollments(
            @PageableDefault(size = 10, sort = "enrolledAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return service.findAllActive(pageable);
    }

    @Operation(summary = "Find enrollment by id")
    @GetMapping("/{id}")
    public EnrollmentResponseDTO getEnrollmentById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "Finish enrollment")
    @PatchMapping("/{id}")
    public EnrollmentResponseDTO finishEnrollment(@PathVariable UUID id) {
        return service.finishEnrollment(id);
    }

    @Operation(summary = "Cancel enrollment")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void cancelEnrollment(@PathVariable UUID id) {
        service.cancel(id);
    }
}
