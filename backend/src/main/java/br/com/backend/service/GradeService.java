package br.com.backend.service;

import br.com.backend.DTO.GradeRequestDTO;
import br.com.backend.DTO.GradeResponseDTO;
import br.com.backend.domain.Grade;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.repository.GradeRepository;
import br.com.backend.util.FunctionsUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GradeService {

    private GradeRepository repository;

    public GradeService(GradeRepository repository) {
        this.repository = repository;
    }

    public GradeResponseDTO create(GradeRequestDTO dto) {
        Grade grade = new Grade(
                dto.getName()
        );
        repository.save(grade);
        return FunctionsUtils.toGradeResponseDTO(grade);
    }

    public List<GradeResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(FunctionsUtils::toGradeResponseDTO)
                .toList();
    }

    public GradeResponseDTO findById(UUID id) {
        Grade grade = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Grade não encontrada"));

        return FunctionsUtils.toGradeResponseDTO(grade);
    }
}
