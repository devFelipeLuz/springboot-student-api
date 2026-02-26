package br.com.backend.service;

import br.com.backend.DTO.EnrollmentRequestDTO;
import br.com.backend.DTO.EnrollmentResponseDTO;
import br.com.backend.domain.Enrollment;
import br.com.backend.domain.EnrollmentStatus;
import br.com.backend.domain.Grade;
import br.com.backend.domain.Student;
import br.com.backend.exception.BusinessException;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.repository.EnorllmentRepository;
import br.com.backend.repository.GradeRepository;
import br.com.backend.repository.StudentRepository;
import br.com.backend.util.FunctionsUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class EnrollmentService {

    private EnorllmentRepository repository;
    private StudentRepository studentRepo;
    private GradeRepository gradeRepo;

    public EnrollmentService(
            EnorllmentRepository repository,
            StudentRepository studentRepo,
            GradeRepository gradeRepo) {

        this.repository = repository;
        this.studentRepo = studentRepo;
        this.gradeRepo = gradeRepo;
    }

    public EnrollmentResponseDTO enroll(EnrollmentRequestDTO dto) {
        Student student = studentRepo.findById(dto.getStudent().getId())
                        .orElseThrow(() -> new EntityNotFoundException("Student não encontrado"));

        Grade grade = gradeRepo.findById(dto.getGrade().getId())
                        .orElseThrow(() -> new EntityNotFoundException("Grade não encontrada"));


        grade.validateCapacity();
        student.validateCanEnroll();

        Enrollment enrollment = new Enrollment(student, grade);

        grade.increaseActiveEnrollmentsCount();
        student.addEnrollment(enrollment);
        grade.addEnrollment(enrollment);

        repository.save(enrollment);

        return FunctionsUtils.enrollmentToResponseDTO(enrollment);
    }

    public List<EnrollmentResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(FunctionsUtils::enrollmentToResponseDTO)
                .toList();
    }

    public List<EnrollmentResponseDTO> findAllByStatusActive() {
        return repository.findAllByStatusActive().stream()
                .map(FunctionsUtils::enrollmentToResponseDTO)
                .toList();
    }

    public EnrollmentResponseDTO findById(UUID id) {
        Enrollment enrollment = findActiveEnrollment(id);
        return FunctionsUtils.enrollmentToResponseDTO(enrollment);
    }

    public void cancel(UUID id) {
        Enrollment enrollment = findActiveEnrollment(id);
        enrollment.getGrade().cancelEnrollment(enrollment);
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
