package br.com.backend.service;

import br.com.backend.dto.request.ClassroomChangeCapacityRequest;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class ClassroomService {

    private final ClassroomRepository repository;
    private final SchoolYearRepository schoolYearRepository;

    public ClassroomService(ClassroomRepository repository,
                            SchoolYearRepository schoolYearRepository) {
        this.repository = repository;
        this.schoolYearRepository = schoolYearRepository;
    }

    public ClassroomResponseDTO register(ClassroomRequestDTO dto) {
        SchoolYear schoolYear = schoolYearRepository.findById(dto.schoolYearId())
                .orElseThrow(() -> new EntityNotFoundException("School Year Not Found"));
        schoolYear.ensureActive();

        Classroom classroom = new Classroom(dto.name(), schoolYear);
        Classroom saved = repository.save(classroom);
        return ClassroomMapper.toDTO(saved);
    }

    public Page<ClassroomResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(ClassroomMapper::toDTO);
    }

    public ClassroomResponseDTO findById(UUID id) {
        Classroom classroom = findClassroom(id);
        return ClassroomMapper.toDTO(classroom);
    }

    public ClassroomResponseDTO changeCapacity(UUID id, ClassroomChangeCapacityRequest dto) {
        Classroom classroom = findClassroom(id);
        classroom.getSchoolYear().ensureActive();
        classroom.changeCapacity(dto.newCapacity());
        return ClassroomMapper.toDTO(classroom);
    }

    public void deactivateClassroom(UUID id) {
        Classroom classroom = findClassroom(id);
        classroom.deactivate();
    }

    private Classroom findClassroom(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found"));
    }
}
