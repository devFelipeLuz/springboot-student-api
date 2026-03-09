package br.com.backend.controller;

import br.com.backend.DTO.user.AdminUserCreateRequestDTO;
import br.com.backend.DTO.user.UserResponseDTO;
import br.com.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin/users")
public class AdminUserController {

    private UserService service;

    public AdminUserController(UserService service) {
        this.service = service;
    }

    @Operation(summary = "Registra um usuário admin")
    @PostMapping
    public UserResponseDTO register(@Valid @RequestBody AdminUserCreateRequestDTO dto) {
        return service.createAdminUser(dto);
    }

    @Operation(summary = "Busca todos os usuários e retorna em páginas")
    @GetMapping
    public Page<UserResponseDTO> findAll(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return service.findAll(pageable);
    }

    @Operation(summary = "Busca todos os usuários habilitados e retorna em páginas")
    @GetMapping
    public Page<UserResponseDTO> findAllEnabled(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return service.findAllEnabled(pageable);
    }

    @Operation(summary = "Busca um usuário por ID")
    @GetMapping("/{id}")
    public UserResponseDTO findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "Atualiza o username de um usuário encontrado por ID")
    @PutMapping("/{id}")
    public UserResponseDTO updateAdminUserUsername(@PathVariable UUID id,
                                                   @Valid @RequestBody AdminUserCreateRequestDTO dto) {
        return service.updateAdminUsername(id, dto);
    }

    @Operation(summary = "Atualiza um password de um usuário encontrado por ID")
    @PutMapping("/{id}")
    public UserResponseDTO updateAdminUserPassword(@PathVariable UUID id,
                                                   @Valid @RequestBody AdminUserCreateRequestDTO dto){
        return service.updateAdminUserPassword(id, dto);
    }

    @Operation(summary = "Deleta um usuário encontrado por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
