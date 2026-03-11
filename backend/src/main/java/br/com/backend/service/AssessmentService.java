package br.com.backend.service;

import br.com.backend.dto.request.AssessmentRequestDTO;
import br.com.backend.dto.response.AssessmentResponseDTO;
import br.com.backend.entity.Assessment;
import br.com.backend.entity.TeachingAssignment;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.mapper.AssessmentMapper;
import br.com.backend.repository.AssessmentRepository;
import br.com.backend.repository.TeachingAssignmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class AssessmentService {

    private final AssessmentRepository repository;
    private final TeachingAssignmentRepository teachingAssignmentRepository;

    public AssessmentService(AssessmentRepository repository,
                             TeachingAssignmentRepository teachingAssignmentRepository) {
        this.repository = repository;
        this.teachingAssignmentRepository = teachingAssignmentRepository;
    }

    public AssessmentResponseDTO register(AssessmentRequestDTO dto) {
        TeachingAssignment teachingAssignment = teachingAssignmentRepository.findById(dto.teachingAssignmentId())
                .orElseThrow(() -> new EntityNotFoundException("TeachingAssignment not found"));

        Assessment assessment = new Assessment(
                dto.title(),
                dto.type(),
                teachingAssignment);

        repository.save(assessment);

        return AssessmentMapper.toDTO(assessment);
    }

    public Page<AssessmentResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(AssessmentMapper::toDTO);
    }

    public AssessmentResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(AssessmentMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Assessment não encontrado"));
    }

    public AssessmentResponseDTO update(UUID id, AssessmentRequestDTO dto) {
        Assessment assessment = findAssessmentById(id);
        TeachingAssignment teachingAssignment = assessment.getTeachingAssignment();

        assessment.updateData(
                dto.title(),
                dto.type(),
                teachingAssignment
        );

        return AssessmentMapper.toDTO(assessment);
    }

    public void delete(UUID id) {
        Assessment assessment = findAssessmentById(id);
        repository.delete(assessment);
    }

    private Assessment findAssessmentById(UUID assessmentId) {
        return repository.findById(assessmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assessment not found"));
    }
}
