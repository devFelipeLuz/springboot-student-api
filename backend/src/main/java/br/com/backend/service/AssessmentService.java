package br.com.backend.service;

import br.com.backend.DTO.assessment.AssessmentRequestDTO;
import br.com.backend.DTO.assessment.AssessmentResponseDTO;
import br.com.backend.domain.Assessment;
import br.com.backend.domain.TeachingAssignment;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.repository.AssessmentRepository;
import br.com.backend.repository.TeachingAssignmentRepository;
import br.com.backend.util.ToResponseDTO;
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

        return ToResponseDTO.toAssessmentResponseDTO(assessment);
    }

    public Page<AssessmentResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(ToResponseDTO::toAssessmentResponseDTO);
    }

    public AssessmentResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(ToResponseDTO::toAssessmentResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("Assessment não encontrado"));
    }

    public AssessmentResponseDTO update(UUID id, AssessmentRequestDTO dto) {
        Assessment assessment = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assessment não encontrado"));

        TeachingAssignment teachingAssignment = assessment.getTeachingAssignment();

        assessment.updateData(
                dto.title(),
                dto.type(),
                teachingAssignment
        );

        repository.save(assessment);
        return ToResponseDTO.toAssessmentResponseDTO(assessment);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
