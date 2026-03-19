package br.com.backend.service;

import br.com.backend.dto.request.AttendanceCreateRequest;
import br.com.backend.dto.request.AttendanceRecordRequest;
import br.com.backend.dto.response.AttendanceSessionResponseDTO;
import br.com.backend.entity.AttendanceSession;
import br.com.backend.entity.Enrollment;
import br.com.backend.entity.TeachingAssignment;
import br.com.backend.exception.BusinessException;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.mapper.AttendanceMapper;
import br.com.backend.repository.AttendanceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class AttendanceService {

    private final AttendanceRepository repository;
    private final TeachingAssignmentService teachingAssignmentService;
    private final EnrollmentService enrollmentService;

    public AttendanceService(AttendanceRepository repository,
                             TeachingAssignmentService teachingAssignmentService,
                             EnrollmentService enrollmentService) {

        this.repository = repository;
        this.teachingAssignmentService = teachingAssignmentService;
        this.enrollmentService = enrollmentService;
    }

    public AttendanceSessionResponseDTO register(AttendanceCreateRequest dto) {
        if (repository.existsByTeachingAssignmentAndDate(dto.teachingAssignmentId(), dto.date())) {
            throw new BusinessException("Session already exists");
        }

        TeachingAssignment assignment = teachingAssignmentService
                .findAssignmentById(dto.teachingAssignmentId());

        Enrollment enrollment = enrollmentService
                .findActiveEnrollmentById(dto.enrollmentId());

        AttendanceSession session = new AttendanceSession(assignment, dto.date());

        session.registerAttendance(enrollment, dto.status());
        AttendanceSession saved = repository.save(session);
        return AttendanceMapper.toDTO(saved);
    }

    public Page<AttendanceSessionResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(AttendanceMapper::toDTO);
    }

    public AttendanceSessionResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(AttendanceMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Attendance Not Found"));
    }

    public AttendanceSessionResponseDTO update (
            UUID sessionId,
            UUID recordId,
            AttendanceRecordRequest recordDto) {

        AttendanceSession session = findAttendanceSessionById(sessionId);

        session.updateAttendance(recordId, recordDto.status());
        return AttendanceMapper.toDTO(session);
    }

    public void delete(UUID sessionId) {
        AttendanceSession session = findAttendanceSessionById(sessionId);
        repository.delete(session);
    }

    public AttendanceSession findAttendanceSessionById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Attendance session Not Found"));
    }
}
