package br.com.backend.service;

import br.com.backend.dto.request.UpdatePasswordRequest;
import br.com.backend.dto.request.UpdateUsernameRequest;
import br.com.backend.dto.request.UserCreateRequest;
import br.com.backend.dto.response.UserResponseDTO;
import br.com.backend.entity.User;
import br.com.backend.entity.enums.Role;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.mapper.UserMapper;
import br.com.backend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repository,
                       PasswordEncoder encoder) {

        this.repository = repository;
        this.encoder = encoder;
    }

    //Usado pelo controller
    public UserResponseDTO register(UserCreateRequest dto) {
        User saved = registerUser(dto.email(), dto.password(), dto.role());
        return UserMapper.toDTO(saved);
    }

    //Usado por services internos
    public User registerUser(String email, String password, Role role) {
        String encodedPassword = encoder.encode(password);
        User user = User.createUser(email, encodedPassword, role);
        return repository.save(user);
    }

    //Usado pelo controller
    public Page<UserResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(UserMapper::toDTO);
    }

    //Usado pelo controller
    public Page<UserResponseDTO> findAllEnabled(Pageable pageable) {
        return repository.findAllByEnabledTrue(pageable)
                .map(UserMapper::toDTO);
    }

    //Usado pelo controller
    public UserResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(UserMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    //Usado pelo controller
    public UserResponseDTO updateEmail(UUID id, UpdateUsernameRequest dto) {
        User user = changeEmail(id, dto.email());
        return UserMapper.toDTO(user);
    }

    //Usado pelo controller
    public UserResponseDTO updatePassword(UUID id, UpdatePasswordRequest dto) {
        User user = changePassword(id, dto.password());
        return UserMapper.toDTO(user);
    }

    //Usado por services internos
    public User changeEmail(UUID id, String email) {
        User user = findActiveUserById(id);
        user.updateEmail(email);
        return user;
    }

    //Usado por services internos
    public User changePassword(UUID id, String password) {
        User user = findActiveUserById(id);
        user.updatePassword(encoder.encode(password));
        return user;
    }

    //Usado pelo controller
    public void deactivate(UUID id) {
        User user = findActiveUserById(id);
        user.deactivate();
    }

    private User findActiveUserById(UUID id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User não encontrado"));
        user.isEnabled();
        return user;
    }
}