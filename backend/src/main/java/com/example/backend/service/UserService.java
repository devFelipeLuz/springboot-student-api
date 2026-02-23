package com.example.backend.service;

import com.example.backend.DTO.AdminUserCreateRequest;
import com.example.backend.DTO.PublicUserRequestDTO;
import com.example.backend.DTO.UserResponseDTO;
import com.example.backend.entity.User;
import com.example.backend.entity.User.Role;
import com.example.backend.exception.BusinessException;
import com.example.backend.exception.EntityNotFoundException;
import com.example.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public UserResponseDTO createAdminUser(AdminUserCreateRequest dto) {
        User adminUser = User.createUser(
                dto.getUsername(),
                dto.getPassword(),
                dto.getRole());

        repository.save(adminUser);

        return toUserResponseDTO(adminUser);
    }

    public UserResponseDTO createPublicUser(PublicUserRequestDTO dto) {
        User user = User.createUser(
                dto.getUsername(),
                dto.getPassword(),
                Role.USER);

        repository.save(user);

        return toUserResponseDTO(user);
    }

    public List<UserResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::toUserResponseDTO)
                .toList();
    }

    public List<UserResponseDTO> findAllEnabled() {
        return repository.findAllByEnabledTrue().stream()
                .map(this::toUserResponseDTO)
                .toList();
    }

    public UserResponseDTO findById(UUID id) {
        User user = findActiveUserById(id);
        return toUserResponseDTO(user);
    }

    public void updateAdminUsername(UUID id, AdminUserCreateRequest dto) {
        User adminUser = findActiveUserById(id);
        adminUser.updateUsername(dto.getUsername());
    }

    public void updateAdminUserPassword(UUID id, AdminUserCreateRequest dto) {
        User adminUser = findActiveUserById(id);
        adminUser.updatePassword(dto.getPassword());
    }

    public void updatePublicUserUsername(UUID id, PublicUserRequestDTO dto) {
        User user = findActiveUserById(id);
        user.updateUsername(dto.getUsername());
        repository.save(user);
    }

    public void updatePublicUserPassword(UUID id, PublicUserRequestDTO dto) {
        User user = findActiveUserById(id);
        user.updatePassword(dto.getPassword());
    }

    public void delete(UUID id) {
        User user = findActiveUserById(id);
        user.deactivate();
    }

    public UserResponseDTO toUserResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }

    public User findActiveUserById(UUID id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User não encontrado"));

        if (!user.getEnabled()) {
            throw new BusinessException("Usuário desabilitado");
        }

        return user;
    }
}