package br.com.backend.service;

import br.com.backend.dto.request.StudentGradeCreateRequest;
import br.com.backend.dto.request.StudentGradeFilter;
import br.com.backend.dto.response.StudentGradeResponseDTO;
import br.com.backend.dto.request.StudentGradeUpdateRequest;
import br.com.backend.entity.Assessment;
import br.com.backend.entity.Enrollment;
import br.com.backend.entity.StudentGrade;
import br.com.backend.exception.BusinessException;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.mapper.StudentGradeMapper;
import br.com.backend.repository.StudentGradeRepository;
import br.com.backend.specification.StudentGradeSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class StudentGradeService {

    private final StudentGradeRepository repository;
    private final AssessmentService assessmentService;
    private final EnrollmentService enrollmentService;

    public StudentGradeService(StudentGradeRepository repository,
                               AssessmentService assessmentService,
                               EnrollmentService enrollmentService) {

        this.repository = repository;
        this.assessmentService = assessmentService;
        this.enrollmentService = enrollmentService;
    }

    public StudentGradeResponseDTO register(StudentGradeCreateRequest dto) {
        if (repository.existsByAssessmentIdAndEnrollmentId(
                dto.assessmentId(), dto.enrollmentId())) {

            throw new BusinessException("Grade already exists for this student and assessment");
        }

        Assessment assessment = assessmentService.findActiveAssessmentById(dto.assessmentId());
        Enrollment enrollment = enrollmentService.findActiveEnrollmentById(dto.enrollmentId());

        StudentGrade grade = new StudentGrade(assessment, enrollment, dto.maxScore());
        StudentGrade saved = repository.save(grade);
        return StudentGradeMapper.toDTO(saved);
    }

    public Page<StudentGradeResponseDTO> findAll(StudentGradeFilter filter, Pageable pageable) {
        Specification<StudentGrade> spec =
                StudentGradeSpecification.withFilters(filter);

        return repository.findAll(spec, pageable)
                .map(StudentGradeMapper::toDTO);
    }

    public StudentGradeResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(StudentGradeMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Grade not found"));
    }

    public Page<StudentGradeResponseDTO> findGradeByAssessmentId(UUID assessmentId, Pageable pageable) {
        return repository.findByAssessmentId(assessmentId, pageable)
                .map(StudentGradeMapper::toDTO);
    }

    public StudentGradeResponseDTO update(UUID id, StudentGradeUpdateRequest dto) {
        StudentGrade grade = findStudentGradeById(id);
        grade.updateGrade(dto.grade());
        return StudentGradeMapper.toDTO(grade);
    }

    public void delete(UUID id) {
        StudentGrade grade = findStudentGradeById(id);
        repository.delete(grade);
    }

    private StudentGrade findStudentGradeById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Grade not found"));
    }
}