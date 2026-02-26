package br.com.backend.controller;

import br.com.backend.DTO.PublicUserCreateRequestDTO;
import br.com.backend.DTO.UserResponseDTO;
import br.com.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService adminService;

    public UserController(UserService AdminService) {
        this.adminService = adminService;
    }

    @PostMapping
    public UserResponseDTO register(@Valid @RequestBody PublicUserCreateRequestDTO dto) {
        return adminService.createPublicUser(dto);
    }

    @PutMapping("/{id}")
    public UserResponseDTO updateStudentUserUsername(@PathVariable UUID id,
                                                    @Valid @RequestBody PublicUserCreateRequestDTO dto) {
        return adminService.updatePublicUserUsername(id, dto);
    }

    @PutMapping("/{id}")
    public UserResponseDTO updatePublicUserPassword(@PathVariable UUID id,
                                                    @Valid @RequestBody PublicUserCreateRequestDTO dto) {
        return adminService.updatePublicUserPassword(id, dto);
    }
}
