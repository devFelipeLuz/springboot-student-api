package br.com.backend.controller;

import br.com.backend.DTO.AdminUserCreateRequestDTO;
import br.com.backend.DTO.UserResponseDTO;
import br.com.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/user")
public class AdminUserController {

    private UserService service;

    public AdminUserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public UserResponseDTO register(@Valid @RequestBody AdminUserCreateRequestDTO dto) {
        return service.createAdminUser(dto);
    }

    @GetMapping
    public List<UserResponseDTO> findAll() {
        return service.findAll();
    }

    @GetMapping
    public List<UserResponseDTO> findAllEnabled() {
        return service.findAllEnabled();
    }

    @GetMapping("/{id}")
    public UserResponseDTO findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public UserResponseDTO updateAdminUserUsername(@PathVariable UUID id,
                                                   @Valid @RequestBody AdminUserCreateRequestDTO dto) {
        return service.updateAdminUsername(id, dto);
    }

    @PutMapping("/{id}")
    public UserResponseDTO updateAdminUserPassword(@PathVariable UUID id,
                                                   @Valid @RequestBody AdminUserCreateRequestDTO dto){
        return service.updateAdminUserPassword(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
