package br.com.backend.service;

import br.com.backend.dto.request.TeachingAssignmentRequest;
import br.com.backend.dto.response.TeachingAssignmetResponseDTO;
import br.com.backend.entity.Classroom;
import br.com.backend.entity.Professor;
import br.com.backend.entity.Subject;
import br.com.backend.entity.TeachingAssignment;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.mapper.TeachingAssignmentMapper;
import br.com.backend.repository.TeachingAssignmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class TeachingAssignmentService {

    private final TeachingAssignmentRepository repository;
    private final ProfessorService professorService;
    private final SubjectService subjectService;
    private final ClassroomService classroomService;

    public TeachingAssignmentService(TeachingAssignmentRepository repository,
                                     ProfessorService professorService,
                                     SubjectService subjectService,
                                     ClassroomService classroomService) {

        this.repository = repository;
        this.professorService = professorService;
        this.subjectService = subjectService;
        this.classroomService = classroomService;
    }

    public TeachingAssignmetResponseDTO register(TeachingAssignmentRequest dto) {
        Professor professor = professorService.findActiveProfessorById(dto.professorId());
        Subject subject = subjectService.findSubjectById(dto.subjectId());
        Classroom classroom = classroomService.findActiveClassroomById(dto.classroomId());

        TeachingAssignment assignment = new TeachingAssignment(professor, subject, classroom);
        TeachingAssignment saved = repository.save(assignment);
        return TeachingAssignmentMapper.toDTO(saved);
    }

    public Page<TeachingAssignmetResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(TeachingAssignmentMapper::toDTO);
    }

    public TeachingAssignmetResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(TeachingAssignmentMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("TeachingAssignment not found"));
    }

    public void delete(UUID id) {
        TeachingAssignment assignment = findAssignmentById(id);
        repository.delete(assignment);
    }

    protected TeachingAssignment findAssignmentById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found"));
    }
}
