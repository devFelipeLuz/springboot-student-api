package br.com.backend.service;

import br.com.backend.dto.request.ClassroomChangeCapacityRequest;
import br.com.backend.dto.request.ClassroomCreateRequest;
import br.com.backend.dto.response.ClassroomResponseDTO;
import br.com.backend.entity.Classroom;
import br.com.backend.entity.SchoolYear;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.mapper.ClassroomMapper;
import br.com.backend.repository.ClassroomRepository;
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
public class ClassroomService {

    private final ClassroomRepository repository;
    private final SchoolYearService schoolYearService;

    public ClassroomService(
            ClassroomRepository repository, SchoolYearService schoolYearService) {

        this.repository = repository;
        this.schoolYearService = schoolYearService;
    }

    public ClassroomResponseDTO register(ClassroomCreateRequest dto) {
        SchoolYear schoolYear = schoolYearService.findActiveSchoolYear(dto.schoolYearId());

        Classroom classroom = new Classroom(dto.name(), schoolYear);
        Classroom saved = repository.save(classroom);
        return ClassroomMapper.toDTO(saved);
    }

    public Page<ClassroomResponseDTO> findAll(String name, Boolean active, Pageable pageable) {
        Map<String, Object> filters = new HashMap<>();
        filters.put("name", name);
        filters.put("active", active);

        Specification<Classroom> spec = GenericSpecification.withFilters(filters);

        return repository.findAll(spec, pageable)
                .map(ClassroomMapper::toDTO);
    }

    public ClassroomResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(ClassroomMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found"));
    }

    public ClassroomResponseDTO changeCapacity(UUID id, ClassroomChangeCapacityRequest dto) {
        Classroom classroom = findActiveClassroomById(id);
        classroom.changeCapacity(dto.newCapacity());
        return ClassroomMapper.toDTO(classroom);
    }

    public void deactivate(UUID id) {
        Classroom classroom = findActiveClassroomById(id);
        classroom.deactivate();
    }

    public Classroom findActiveClassroomById(UUID id) {
        Classroom classroom = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Classroom Not Found"));
        classroom.ensureActive();
        return classroom;
    }
}
