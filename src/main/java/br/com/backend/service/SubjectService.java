package br.com.backend.service;

import br.com.backend.dto.request.SubjectRequest;
import br.com.backend.dto.response.SubjectResponseDTO;
import br.com.backend.entity.Subject;
import br.com.backend.exception.BusinessException;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.mapper.SubjectMapper;
import br.com.backend.repository.SubjectRepository;
import br.com.backend.specification.GenericSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class SubjectService {

    private final SubjectRepository repository;

    public SubjectService(SubjectRepository repository) {
        this.repository = repository;
    }

    public SubjectResponseDTO register(SubjectRequest dto) {
        if (repository.existsByNameIgnoreCase(dto.name())) {
            throw new BusinessException("Subject already exists");
        }

        Subject subject = new Subject(dto.name());
        Subject saved = repository.save(subject);
        return SubjectMapper.toDTO(saved);
    }

    public SubjectResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(SubjectMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Subject not found"));
    }

    public Page<SubjectResponseDTO> findAll(String subjectName, Boolean active, Pageable pageable) {
        Map<String, Object> filters = new HashMap<>();
        filters.put("name", subjectName);
        filters.put("active", active);

        Specification<Subject> spec =
                GenericSpecification.withFilters(filters);

        return repository.findAll(spec, pageable)
                .map(SubjectMapper::toDTO);
    }

    public SubjectResponseDTO updateName(UUID id, SubjectRequest dto) {
        if (repository.existsByNameIgnoreCase(dto.name())) {
            throw new BusinessException("Subject already exists");
        }

        Subject subject = findActiveSubjectById(id);
        subject.updateName(dto.name());
        return SubjectMapper.toDTO(subject);
    }

    public void deactivate(UUID id) {
        Subject subject = findActiveSubjectById(id);
        subject.deactivate();
    }

    public Subject findActiveSubjectById(UUID id) {
        Subject subject = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subject not found"));
        subject.ensureActive();
        return subject;
    }
}
