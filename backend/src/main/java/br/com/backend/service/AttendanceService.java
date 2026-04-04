package br.com.backend.service;

import br.com.backend.dto.request.AttendanceCreateRequest;
import br.com.backend.dto.request.AttendanceRecordRequest;
import br.com.backend.dto.response.AttendanceRecordResponseDTO;
import br.com.backend.dto.response.AttendanceSessionResponseDTO;
import br.com.backend.entity.AttendanceRecord;
import br.com.backend.entity.AttendanceSession;
import br.com.backend.entity.Enrollment;
import br.com.backend.entity.TeachingAssignment;
import br.com.backend.entity.enums.AttendanceStatus;
import br.com.backend.exception.BusinessException;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.mapper.AttendanceRecordMapper;
import br.com.backend.mapper.AttendanceSessionMapper;
import br.com.backend.repository.AttendanceRecordRepository;
import br.com.backend.repository.AttendanceSessionRepository;
import br.com.backend.specification.AttendanceRecordSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class AttendanceService {

    private final AttendanceSessionRepository sessionRepository;
    private final AttendanceRecordRepository recordRepository;
    private final TeachingAssignmentService teachingAssignmentService;
    private final EnrollmentService enrollmentService;

    public AttendanceService(AttendanceSessionRepository sessionRepository,
                             AttendanceRecordRepository recordRepository,
                             TeachingAssignmentService teachingAssignmentService,
                             EnrollmentService enrollmentService) {

        this.sessionRepository = sessionRepository;
        this.recordRepository = recordRepository;
        this.teachingAssignmentService = teachingAssignmentService;
        this.enrollmentService = enrollmentService;
    }

    public AttendanceSessionResponseDTO register(AttendanceCreateRequest dto) {
        if (sessionRepository.existsByTeachingAssignment_IdAndDate(dto.teachingAssignmentId(), dto.date())) {
            throw new BusinessException("Session already exists");
        }

        TeachingAssignment assignment = teachingAssignmentService
                .findAssignmentById(dto.teachingAssignmentId());

        Enrollment enrollment = enrollmentService
                .findActiveEnrollmentById(dto.enrollmentId());

        AttendanceSession session = new AttendanceSession(assignment, dto.date());

        session.registerAttendance(enrollment, dto.status());
        AttendanceSession saved = sessionRepository.save(session);
        return AttendanceSessionMapper.toDTO(saved);
    }

    public Page<AttendanceRecordResponseDTO> findAll(
            String studentName, String studentEmail, AttendanceStatus status, Pageable pageable) {

        Specification<AttendanceRecord> spec =
                AttendanceRecordSpecification.withFilters(studentName, studentEmail, status);

        return recordRepository.findAll(spec, pageable)
                .map(AttendanceRecordMapper::toDTO);
    }

    public AttendanceSessionResponseDTO findById(UUID id) {
        return sessionRepository.findById(id)
                .map(AttendanceSessionMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Attendance Not Found"));
    }

    public AttendanceSessionResponseDTO update (
            UUID sessionId,
            UUID recordId,
            AttendanceRecordRequest recordDto) {

        AttendanceSession session = findAttendanceSessionById(sessionId);

        session.updateAttendance(recordId, recordDto.status());
        return AttendanceSessionMapper.toDTO(session);
    }

    public void delete(UUID sessionId) {
        AttendanceSession session = findAttendanceSessionById(sessionId);
        sessionRepository.delete(session);
    }

    public AttendanceSession findAttendanceSessionById(UUID id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Attendance session Not Found"));
    }
}
