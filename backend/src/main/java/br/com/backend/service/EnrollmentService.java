package br.com.backend.service;

import br.com.backend.DTO.enrollment.EnrollmentRequestDTO;
import br.com.backend.DTO.enrollment.EnrollmentResponseDTO;
import br.com.backend.domain.*;
import br.com.backend.domain.enums.EnrollmentStatus;
import br.com.backend.exception.BusinessException;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.repository.*;
import br.com.backend.util.ToResponseDTO;
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
        Student student = studentRepo.findById(dto.getStudent().getId())
                        .orElseThrow(() -> new EntityNotFoundException("Student não encontrado"));

        Classroom classroom = classRepository.findById(dto.getClassroom().getId())
                        .orElseThrow(() -> new EntityNotFoundException("Grade não encontrada"));

        SchoolYear schoolYear = schoolYearRepository.findById(dto.getSchoolYear().getId())
                        .orElseThrow(() -> new EntityNotFoundException("SchoolYear não encontrado"));


        classroom.validateCapacity();
        student.validateCanEnroll();

        Enrollment enrollment = new Enrollment(student, classroom, schoolYear);

        classroom.increaseActiveEnrollmentsCount();
        student.addEnrollment(enrollment);
        classroom.addEnrollment(enrollment);

        repository.save(enrollment);

        return ToResponseDTO.toEnrollmentResponseDTO(enrollment);
    }

    public Page<EnrollmentResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(ToResponseDTO::toEnrollmentResponseDTO);
    }

    public Page<EnrollmentResponseDTO> findAllByStatusActive(Pageable pageable) {
        return repository.findByStatusActive(pageable)
                .map(ToResponseDTO::toEnrollmentResponseDTO);
    }

    public EnrollmentResponseDTO findById(UUID id) {
        Enrollment enrollment = findActiveEnrollment(id);
        return ToResponseDTO.toEnrollmentResponseDTO(enrollment);
    }

    public void cancel(UUID id) {
        Enrollment enrollment = findActiveEnrollment(id);
        enrollment.getClassroom().cancelEnrollment(enrollment);
        repository.save(enrollment);
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
