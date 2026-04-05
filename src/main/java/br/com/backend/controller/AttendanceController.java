package br.com.backend.controller;

import br.com.backend.dto.request.AttendanceCreateRequest;
import br.com.backend.dto.request.AttendanceRecordRequest;
import br.com.backend.dto.response.AttendanceRecordResponseDTO;
import br.com.backend.dto.response.AttendanceSessionResponseDTO;
import br.com.backend.entity.enums.AttendanceStatus;
import br.com.backend.service.AttendanceService;
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
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService service;

    public AttendanceController(AttendanceService service) {
        this.service = service;
    }

    @Operation(summary = "Create attendance")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public AttendanceSessionResponseDTO registerAttendance(@Valid @RequestBody AttendanceCreateRequest dto) {
        return service.register(dto);
    }

    @Operation(summary = "Find attendance by id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public AttendanceSessionResponseDTO getAttendanceById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "List attendances")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public Page<AttendanceRecordResponseDTO> getAttendances(
            @Parameter(description = "Filter by student's name")
            @RequestParam(required = false) String studentName,

            @Parameter(description = "Filter by student's email")
            @RequestParam(required = false)
            String studentEmail,

            @Parameter(description = "Filter by attendance status (PRESENT, ABSENT or JUSTIFIED_ABSENCE)")
            @RequestParam(required = false) AttendanceStatus status,

            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return service.findAll(studentName, studentEmail, status, pageable);
    }

    @Operation(summary = "Update attendance")
    @PatchMapping("/{sessionId}/records/{recordId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public AttendanceSessionResponseDTO updateAttendance(
            @PathVariable UUID sessionId,
            @PathVariable UUID recordId,
            @Valid @RequestBody AttendanceRecordRequest recordDto) {

        return service.update(sessionId, recordId, recordDto);
    }

    @Operation(summary = "Delete attendance")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public void deleteAttendance(@PathVariable UUID id) {
        service.delete(id);
    }
}
