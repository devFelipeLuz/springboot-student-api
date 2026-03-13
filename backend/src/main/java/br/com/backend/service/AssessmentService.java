package br.com.backend.service;

import br.com.backend.dto.request.AssessmentCreateRequest;
import br.com.backend.dto.request.AssessmentUpdateRequest;
import br.com.backend.dto.response.AssessmentResponseDTO;
import br.com.backend.entity.Assessment;
import br.com.backend.entity.TeachingAssignment;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.mapper.AssessmentMapper;
import br.com.backend.repository.AssessmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class AssessmentService {

    private final AssessmentRepository repository;
    private final TeachingAssignmentService teachingAssignmentService;

    public AssessmentService(AssessmentRepository repository,
                             TeachingAssignmentService teachingAssignmentService) {

        this.repository = repository;
        this.teachingAssignmentService = teachingAssignmentService;
    }

    public AssessmentResponseDTO register(AssessmentCreateRequest dto) {
        TeachingAssignment assignment = teachingAssignmentService
                .findAssignmentById(dto.teachingAssignmentId());

        assignment.ensureAllIsActive();

        Assessment assessment = new Assessment(assignment, dto.title(), dto.type());

        Assessment saved = repository.save(assessment);
        return AssessmentMapper.toDTO(saved);
    }

    public Page<AssessmentResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(AssessmentMapper::toDTO);
    }

    public AssessmentResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(AssessmentMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Assessment not found"));
    }

    public AssessmentResponseDTO update(UUID id, AssessmentUpdateRequest dto) {
        Assessment assessment = findAssessmentById(id);

        if (dto.title() != null) {
            assessment.updateTitle(dto.title());
        }

        if (dto.type() != null) {
            assessment.updateType(dto.type());
        }

        return AssessmentMapper.toDTO(assessment);
    }

    public void delete(UUID id) {
        Assessment assessment = findAssessmentById(id);
        repository.delete(assessment);
    }

    protected Assessment findAssessmentById(UUID assessmentId) {
        return repository.findById(assessmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assessment not found"));
    }
}
