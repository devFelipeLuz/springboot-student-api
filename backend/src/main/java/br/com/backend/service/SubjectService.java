package br.com.backend.service;

import br.com.backend.dto.request.SubjectRequest;
import br.com.backend.dto.response.SubjectResponseDTO;
import br.com.backend.entity.Subject;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.mapper.SubjectMapper;
import br.com.backend.repository.SubjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class SubjectService {

    private final SubjectRepository repository;

    public SubjectService(SubjectRepository repository) {
        this.repository = repository;
    }

    public SubjectResponseDTO register(SubjectRequest dto) {
        Subject subject = new Subject(dto.name());
        Subject saved = repository.save(subject);
        return SubjectMapper.toDTO(saved);
    }

    public Page<SubjectResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(SubjectMapper::toDTO);
    }

    public SubjectResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(SubjectMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Subject not found"));
    }

    public SubjectResponseDTO updateName(UUID id, SubjectRequest dto) {
        Subject subject = findSubjectById(id);
        subject.updateName(dto.name());
        return SubjectMapper.toDTO(subject);
    }

    public void delete(UUID id) {
        Subject subject = findSubjectById(id);
        repository.delete(subject);
    }

    protected Subject findSubjectById(UUID id) {
        return  repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subject not found"));
    }
}
