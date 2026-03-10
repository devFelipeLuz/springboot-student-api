package br.com.backend.service;

import br.com.backend.DTO.grade.StudentGradeRequestDTO;
import br.com.backend.DTO.grade.StudentGradeResponseDTO;
import br.com.backend.DTO.grade.GradeUpdateDTO;
import br.com.backend.domain.Assessment;
import br.com.backend.domain.Enrollment;
import br.com.backend.domain.StudentGrade;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.repository.AssessmentRepository;
import br.com.backend.repository.EnrollmentRepository;
import br.com.backend.repository.StudentGradeRepository;
import br.com.backend.util.ToResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class StudentGradeService {

    private final StudentGradeRepository repository;
    private final AssessmentRepository assessmentRepository;
    private final EnrollmentRepository  enrollmentRepository;

    public StudentGradeService(StudentGradeRepository repository,
                               AssessmentRepository assessmentRepository,
                               EnrollmentRepository enrollmentRepository) {

        this.repository = repository;
        this.assessmentRepository = assessmentRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public StudentGradeResponseDTO register(StudentGradeRequestDTO dto) {
        if (repository.existsByAssessmentIdAndEnrollmentId(
                 dto.assessmentId(), dto.enrollmentId())) {

            throw new IllegalStateException("Grade already exists for this student and assessment");
        }

        Assessment assessment = assessmentRepository.findById(dto.assessmentId())
                .orElseThrow(() -> new EntityNotFoundException("Assessment not found"));

        Enrollment enrollment = enrollmentRepository.findById(dto.enrollmentId())
                .orElseThrow(() -> new EntityNotFoundException("Enrollment not found"));

        StudentGrade grade = new StudentGrade(assessment, enrollment);
        repository.save(grade);
        return ToResponseDTO.toStudentGradeResponseDTO(grade);
    }

    public Page<StudentGradeResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(ToResponseDTO::toStudentGradeResponseDTO);
    }

    public StudentGradeResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(ToResponseDTO::toStudentGradeResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("Grade not found"));
    }

    public StudentGradeResponseDTO update(UUID id, GradeUpdateDTO dto) {
        StudentGrade grade = findActiveStudentGrade(id);
        grade.setGrade(dto.grade());
        return ToResponseDTO.toStudentGradeResponseDTO(grade);
    }

    public void delete(UUID id) {
        StudentGrade grade = findActiveStudentGrade(id);
        repository.delete(grade);
    }

    public Page<StudentGradeResponseDTO> findByAssessmentId(UUID assessmentId, Pageable pageable) {
        return repository.findByAssessmentId(assessmentId, pageable)
                .map(ToResponseDTO::toStudentGradeResponseDTO);
    }

    private StudentGrade findActiveStudentGrade(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Grade not found"));
    }
}
