package br.com.backend.service;

import br.com.backend.dto.request.SchoolYearRequest;
import br.com.backend.dto.response.SchoolYearResponseDTO;
import br.com.backend.entity.SchoolYear;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.mapper.SchoolYearMapper;
import br.com.backend.repository.SchoolYearRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class SchooYearService {

    private final SchoolYearRepository repository;

    public SchooYearService(SchoolYearRepository repository) {
        this.repository = repository;
    }

    public SchoolYearResponseDTO register(SchoolYearRequest dto) {
        SchoolYear schoolYear = new SchoolYear(dto.year());
        SchoolYear saved = repository.save(schoolYear);
        return SchoolYearMapper.toDTO(saved);
    }

    public Page<SchoolYearResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(SchoolYearMapper::toDTO);
    }

    public SchoolYearResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(SchoolYearMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("SchoolYear not found"));
    }

    public SchoolYearResponseDTO update(UUID id, SchoolYearRequest dto) {
        SchoolYear schoolYear = findActiveSchoolYear(id);
        schoolYear.updateYear(dto.year());
        return SchoolYearMapper.toDTO(schoolYear);
    }

    public void deactivate(UUID id) {
        SchoolYear schoolYear = findActiveSchoolYear(id);
        schoolYear.deactivate();
    }

    protected SchoolYear findActiveSchoolYear(UUID id) {
        SchoolYear schoolYear = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SchoolYear not found"));
        schoolYear.ensureActive();
        return schoolYear;
    }
}
