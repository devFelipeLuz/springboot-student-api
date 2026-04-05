package br.com.backend.dto.request;

import br.com.backend.entity.enums.AttendanceStatus;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AttendanceRecordRequest(
        @NotNull(message = "enrollmentID is required")
        UUID enrollmentId,

        @NotNull(message = "status is required")
        AttendanceStatus status
) {}
