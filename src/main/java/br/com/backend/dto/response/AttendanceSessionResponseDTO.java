package br.com.backend.dto.response;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record AttendanceSessionResponseDTO(
        UUID id,
        String professor,
        String subject,
        String classroom,
        LocalDate date,
        List<AttendanceRecordResponseDTO> students
) {}
