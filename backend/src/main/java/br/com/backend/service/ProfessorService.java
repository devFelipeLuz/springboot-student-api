package br.com.backend.service;

import br.com.backend.dto.request.ProfessorCreateRequest;
import br.com.backend.dto.request.ProfessorUpdateRequest;
import br.com.backend.dto.response.ProfessorResponseDTO;
import br.com.backend.entity.Professor;
import br.com.backend.entity.User;
import br.com.backend.entity.enums.Role;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.mapper.ProfessorMapper;
import br.com.backend.repository.ProfessorRepository;
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
public class ProfessorService {

    private final ProfessorRepository repository;
    private final UserService userService;

    public ProfessorService(ProfessorRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public ProfessorResponseDTO register(ProfessorCreateRequest dto) {
        User user = userService.registerUser(dto.email(), dto.password(), Role.PROFESSOR);

        Professor professor = new Professor(dto.name(), user);
        Professor saved = repository.save(professor);

        return ProfessorMapper.toDTO(saved);
    }

    public ProfessorResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(ProfessorMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Professor not found"));
    }

    public Page<ProfessorResponseDTO> findAll(String name, String email, Boolean active, Pageable pageable) {
        Map<String, Object> filters = new HashMap<>();
        filters.put("name", name);
        filters.put("email", email);
        filters.put("active", active);

        Specification<Professor> spec = GenericSpecification.withFilters(filters);

        return repository.findAll(spec, pageable)
                .map(ProfessorMapper::toDTO);
    }

    public ProfessorResponseDTO update(UUID id, ProfessorUpdateRequest dto) {
        Professor professor = findActiveProfessorById(id);

        if (dto.name() != null) {
            professor.updateName(dto.name());
        }

        if (dto.email() != null) {
            userService.changeEmail(professor.getUser().getId(), dto.email());
        }

        if (dto.password() != null) {
            userService.changePassword(professor.getUser().getId(), dto.password());
        }

        return ProfessorMapper.toDTO(professor);
    }

    public void deactivate(UUID id) {
        Professor professor = findActiveProfessorById(id);
        professor.deactivate();
    }

    protected Professor findActiveProfessorById(UUID professorId) {
        Professor professor = repository.findById(professorId)
                .orElseThrow(() -> new EntityNotFoundException("Professor not found"));
        professor.ensureActive();
        return professor;
    }
}
