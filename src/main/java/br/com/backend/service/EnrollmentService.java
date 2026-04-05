package br.com.backend.service;

import br.com.backend.dto.request.EnrollmentRequest;
import br.com.backend.dto.response.EnrollmentResponseDTO;
import br.com.backend.entity.*;
import br.com.backend.entity.enums.EnrollmentStatus;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.mapper.EnrollmentMapper;
import br.com.backend.repository.*;
import br.com.backend.specification.EnrollmentSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class EnrollmentService {

    private final EnrollmentRepository repository;
    private final StudentService studentService;
    private final ClassroomService classroomService;
    private final SchoolYearService schoolYearService;

    public EnrollmentService(EnrollmentRepository repository,
                             StudentService studentService,
                             SchoolYearService schoolYearService,
                             ClassroomService classroomService) {

        this.repository = repository;
        this.studentService = studentService;
        this.classroomService = classroomService;
        this.schoolYearService = schoolYearService;
    }

    public EnrollmentResponseDTO enroll(EnrollmentRequest dto) {
        Student student = studentService.findActiveStudentById(dto.studentId());
        SchoolYear schoolYear = schoolYearService.findActiveSchoolYear(dto.schoolYearId());
        Classroom classroom = classroomService.findActiveClassroomById(dto.classroomId());


        Enrollment enrollment = new Enrollment(student, schoolYear, classroom);
        enrollment.register();
        Enrollment saved = repository.save(enrollment);
        return EnrollmentMapper.toDTO(saved);
    }

    public EnrollmentResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(EnrollmentMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Enrollment not found"));
    }

    public Page<EnrollmentResponseDTO> findAll(String studentName, EnrollmentStatus status, Pageable pageable) {
        Specification<Enrollment> spec =
                EnrollmentSpecification.withFilters(studentName, status);

        return repository.findAll(spec, pageable)
                .map(EnrollmentMapper::toDTO);
    }

    public EnrollmentResponseDTO finishEnrollment(UUID id) {
        Enrollment enrollment = findActiveEnrollmentById(id);
        enrollment.finishEnrollment();
        return EnrollmentMapper.toDTO(enrollment);
    }

    public EnrollmentResponseDTO cancel(UUID id) {
        Enrollment enrollment = findActiveEnrollmentById(id);
        enrollment.cancelEnrollment();
        return  EnrollmentMapper.toDTO(enrollment);
    }

    public Enrollment findActiveEnrollmentById(UUID id) {
        Enrollment enrollment = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Matrícula não encontrada"));
        enrollment.ensureActive();
        return enrollment;
    }
}
