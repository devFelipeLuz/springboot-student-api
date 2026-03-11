package br.com.backend.service;

import br.com.backend.dto.request.EnrollmentRequestDTO;
import br.com.backend.dto.response.EnrollmentResponseDTO;
import br.com.backend.entity.*;
import br.com.backend.entity.enums.EnrollmentStatus;
import br.com.backend.exception.BusinessException;
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

    private EnrollmentRepository repository;
    private StudentRepository studentRepo;
    private ClassroomRepository classRepository;
    private SchoolYearRepository schoolYearRepository;

    public EnrollmentService(
            EnrollmentRepository repository,
            StudentRepository studentRepo,
            ClassroomRepository classRepository,
            SchoolYearRepository schoolYearRepository) {

        this.repository = repository;
        this.studentRepo = studentRepo;
        this.classRepository = classRepository;
        this.schoolYearRepository = schoolYearRepository;
    }

    public EnrollmentResponseDTO enroll(EnrollmentRequestDTO dto) {
        Student student = studentRepo.findById(dto.studentId())
                        .orElseThrow(() -> new EntityNotFoundException("Student não encontrado"));

        Classroom classroom = classRepository.findById(dto.classroomId())
                        .orElseThrow(() -> new EntityNotFoundException("Grade não encontrada"));

        SchoolYear schoolYear = schoolYearRepository.findById(dto.schoolYearId())
                        .orElseThrow(() -> new EntityNotFoundException("SchoolYear não encontrado"));


        classroom.validateCapacity();
        student.validateCanEnroll();

        Enrollment enrollment = new Enrollment(student, classroom, schoolYear);

        student.addEnrollment(enrollment);
        classroom.addEnrollment(enrollment);

        repository.save(enrollment);

        return EnrollmentMapper.toDTO(enrollment);
    }

    public Page<EnrollmentResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(EnrollmentMapper::toDTO);
    }

    public Page<EnrollmentResponseDTO> findAllByStatusActive(Pageable pageable) {
        return repository.findByStatusActive(pageable)
                .map(EnrollmentMapper::toDTO);
    }

    public EnrollmentResponseDTO findById(UUID id) {
        Enrollment enrollment = findActiveEnrollment(id);
        return EnrollmentMapper.toDTO(enrollment);
    }

    public void cancel(UUID id) {
        Enrollment enrollment = findActiveEnrollment(id);
        enrollment.getClassroom().cancelEnrollment(enrollment);
    }

    public Enrollment findActiveEnrollment(UUID id) {
        Enrollment enrollment = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Matrícula não encontrada"));

        if (enrollment.getStatus().equals(EnrollmentStatus.CANCELED)) {
            throw new BusinessException("Esta matrícula já estava cancelada");
        }

        return enrollment;
    }
}
