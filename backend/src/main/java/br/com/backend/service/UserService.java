package br.com.backend.service;

import br.com.backend.DTO.AdminUserCreateRequestDTO;
import br.com.backend.DTO.PublicUserCreateRequestDTO;
import br.com.backend.DTO.UserResponseDTO;
import br.com.backend.domain.Role;
import br.com.backend.domain.User;
import br.com.backend.exception.BusinessException;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.repository.UserRepository;
import br.com.backend.util.FunctionsUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public UserResponseDTO createAdminUser(AdminUserCreateRequestDTO dto) {
        String encodedPassword = encoder.encode(dto.getPassword());

        User adminUser = User.createAdminUser(
                dto.getUsername(),
                encodedPassword,
                Role.ADMIN);

        repository.save(adminUser);

        return FunctionsUtils.toUserResponseDTO(adminUser);
    }

    public UserResponseDTO createPublicUser(PublicUserCreateRequestDTO dto) {
        String encodedPassword = encoder.encode(dto.getPassword());

        User user = User.createGlobalUser(
                dto.getUsername(),
                encodedPassword);

        repository.save(user);

        return FunctionsUtils.toUserResponseDTO(user);
    }

    public List<UserResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(FunctionsUtils::toUserResponseDTO)
                .toList();
    }

    public List<UserResponseDTO> findAllEnabled() {
        return repository.findAllByEnabledTrue().stream()
                .map(FunctionsUtils::toUserResponseDTO)
                .toList();
    }

    public UserResponseDTO findById(UUID id) {
        User user = findActiveUserById(id);
        return FunctionsUtils.toUserResponseDTO(user);
    }

    public UserResponseDTO updateAdminUsername(UUID id, AdminUserCreateRequestDTO dto) {
        User adminUser = findActiveUserById(id);
        adminUser.updateUsername(dto.getUsername());
        return FunctionsUtils.toUserResponseDTO(adminUser);
    }

    public UserResponseDTO updateAdminUserPassword(UUID id, AdminUserCreateRequestDTO dto) {
        String encondedPassword = encoder.encode(dto.getPassword());
        User adminUser = findActiveUserById(id);
        adminUser.updatePassword(encondedPassword);
        return FunctionsUtils.toUserResponseDTO(adminUser);
    }

    public UserResponseDTO updatePublicUserUsername(UUID id, PublicUserCreateRequestDTO dto) {
        User user = findActiveUserById(id);
        user.updateUsername(dto.getUsername());
        return FunctionsUtils.toUserResponseDTO(user);
    }

    public UserResponseDTO updatePublicUserPassword(UUID id, PublicUserCreateRequestDTO dto) {
        String encodedPassword = encoder.encode(dto.getPassword());
        User user = findActiveUserById(id);
        user.updatePassword(encodedPassword);
        return FunctionsUtils.toUserResponseDTO(user);
    }

    public void delete(UUID id) {
        User user = findActiveUserById(id);
        user.deactivate();
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