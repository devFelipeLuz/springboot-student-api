package br.com.backend.dto.request;

import br.com.backend.entity.enums.AttendanceStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record AttendanceCreateRequest(
        @NotNull(message = "teaching_assignment_id is required")
        UUID teachingAssignmentId,

        @NotNull(message = "date is required")
        LocalDate date,

        @NotNull(message = "enrollmentID is required")
        UUID enrollmentId,

        @NotNull(message = "status is required")
        AttendanceStatus status
) {
}
