package br.com.backend.service;

import br.com.backend.DTO.professor.ProfessorRequestDTO;
import br.com.backend.DTO.professor.ProfessorResponseDTO;
import br.com.backend.domain.Professor;
import br.com.backend.domain.User;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.repository.ProfessorRepository;
import br.com.backend.repository.UserRepository;
import br.com.backend.util.ToResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class ProfessorService {

    private final ProfessorRepository repository;
    private final UserRepository userRepository;

    public ProfessorService(ProfessorRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public ProfessorResponseDTO register(ProfessorRequestDTO dto) {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Professor professor = new Professor(dto.name(), user);
        repository.save(professor);
        return ToResponseDTO.toProfessorResponseDTO(professor);
    }

    public Page<ProfessorResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(ToResponseDTO::toProfessorResponseDTO);
    }

    public ProfessorResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(ToResponseDTO::toProfessorResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("Professor not found"));
    }

    public ProfessorResponseDTO update(UUID id, ProfessorRequestDTO dto) {
        Professor professor = findProfessor(id);
        professor.updateName(dto.name());
        return ToResponseDTO.toProfessorResponseDTO(professor);
    }

    public void delete(UUID id) {
        Professor professor = findProfessor(id);
        professor.deactivate();
    }

    private Professor findProfessor(UUID professorId) {
        return repository.findById(professorId)
                .orElseThrow(() -> new EntityNotFoundException("Professor not found"));
    }
}
