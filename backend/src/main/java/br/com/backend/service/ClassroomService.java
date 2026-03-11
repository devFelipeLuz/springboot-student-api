package br.com.backend.service;

import br.com.backend.dto.request.ClassroomRequestDTO;
import br.com.backend.dto.response.ClassroomResponseDTO;
import br.com.backend.entity.Classroom;
import br.com.backend.entity.SchoolYear;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.mapper.ClassroomMapper;
import br.com.backend.repository.ClassroomRepository;
import br.com.backend.repository.SchoolYearRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClassroomService {

    private final ClassroomRepository repository;
    private final SchoolYearRepository schoolYearRepository;

    public ClassroomService(ClassroomRepository repository, SchoolYearRepository schoolYearRepository) {
        this.repository = repository;
        this.schoolYearRepository = schoolYearRepository;
    }

    public ClassroomResponseDTO register(ClassroomRequestDTO dto) {
        SchoolYear schoolYear = schoolYearRepository.findById(dto.schoolYearId())
                .orElseThrow(() -> new EntityNotFoundException("School Year Not Found"));

        Classroom classroom = new Classroom(dto.name(), schoolYear);
        repository.save(classroom);
        return ClassroomMapper.toDTO(classroom);
    }

    public Page<ClassroomResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(ClassroomMapper::toDTO);
    }

    public ClassroomResponseDTO findById(UUID id) {
        Classroom classroom = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Classroom não encontrada"));

        return ClassroomMapper.toDTO(classroom);
    }
}
