package br.com.backend.service;

import br.com.backend.dto.request.EnrollmentRequest;
import br.com.backend.dto.response.EnrollmentResponseDTO;
import br.com.backend.entity.*;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.mapper.EnrollmentMapper;
import br.com.backend.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class EnrollmentService {

    private final EnrollmentRepository repository;
    private final StudentService studentService;
    private final ClassroomService classroomService;
    private final SchooYearService schooYearService;

    public EnrollmentService(EnrollmentRepository repository,
                             StudentService studentService,
                             ClassroomService classroomService,
                             SchooYearService schooYearService) {

        this.repository = repository;
        this.studentService = studentService;
        this.classroomService = classroomService;
        this.schooYearService = schooYearService;
    }

    public EnrollmentResponseDTO enroll(EnrollmentRequest dto) {
        Student student = studentService.findActiveStudentById(dto.studentId());
        Classroom classroom = classroomService.findActiveClassroomById(dto.classroomId());
        SchoolYear schoolYear = schooYearService.findActiveSchoolYear(dto.schoolYearId());

        classroom.ensureCapacity();
        student.ensureCanEnroll();

        Enrollment enrollment = new Enrollment(student, classroom, schoolYear);
        enrollment.register();
        Enrollment saved = repository.save(enrollment);
        return EnrollmentMapper.toDTO(saved);
    }

    public Page<EnrollmentResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(EnrollmentMapper::toDTO);
    }

    public Page<EnrollmentResponseDTO> findAllActive(Pageable pageable) {
        return repository.findByStatusActive(pageable)
                .map(EnrollmentMapper::toDTO);
    }

    public EnrollmentResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(EnrollmentMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Enrollment not found"));
    }

    public EnrollmentResponseDTO finishEnrollment(UUID id) {
        Enrollment enrollment = findActiveEnrollmentById(id);
        enrollment.finishEnrollment();
        return EnrollmentMapper.toDTO(enrollment);
    }

    public void cancel(UUID id) {
        Enrollment enrollment = findActiveEnrollmentById(id);
        enrollment.cancelEnrollment();
    }

    protected Enrollment findActiveEnrollmentById(UUID id) {
        Enrollment enrollment = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Matrícula não encontrada"));
        enrollment.ensureActive();
        return enrollment;
    }
}
