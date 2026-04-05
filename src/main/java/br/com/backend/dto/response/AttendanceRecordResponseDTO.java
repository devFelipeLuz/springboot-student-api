package br.com.backend.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record AttendanceRecordResponseDTO(
        UUID id,
        String studentName,
        String status,
        LocalDate date
) {}
