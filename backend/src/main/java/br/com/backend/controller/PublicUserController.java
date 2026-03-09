package br.com.backend.controller;

import br.com.backend.DTO.user.PublicUserCreateRequestDTO;
import br.com.backend.DTO.user.UserResponseDTO;
import br.com.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class PublicUserController {

    private UserService adminService;

    public PublicUserController(UserService AdminService) {
        this.adminService = adminService;
    }

    @Operation(summary = "Registra um User público")
    @PostMapping
    public UserResponseDTO registerPublicUsername(@Valid @RequestBody PublicUserCreateRequestDTO dto) {
        return adminService.createPublicUser(dto);
    }

    @Operation(summary = "Atualiza username de User público encontrado por ID")
    @PutMapping("/{id}")
    public UserResponseDTO updatePublicUserUsername(@PathVariable UUID id,
                                                    @Valid @RequestBody PublicUserCreateRequestDTO dto) {
        return adminService.updatePublicUserUsername(id, dto);
    }

    @Operation(summary = "Atualiza password de User público encontrado por ID")
    @PutMapping("/{id}")
    public UserResponseDTO updatePublicUserPassword(@PathVariable UUID id,
                                                    @Valid @RequestBody PublicUserCreateRequestDTO dto) {
        return adminService.updatePublicUserPassword(id, dto);
    }
}
