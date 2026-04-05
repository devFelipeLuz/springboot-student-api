package br.com.backend.mapper;

import br.com.backend.dto.response.AttendanceRecordResponseDTO;
import br.com.backend.entity.AttendanceRecord;

public class AttendanceRecordMapper {

    private AttendanceRecordMapper() {
        throw new UnsupportedOperationException("Mapper");
    }

    public static AttendanceRecordResponseDTO toDTO(AttendanceRecord record) {

        return new AttendanceRecordResponseDTO(
                record.getId(),
                record.getEnrollment().getStudent().getName(),
                record.getStatus().name(),
                record.getAttendanceSession().getDate()
        );
    }
}
