package br.com.backend.controller;

import br.com.backend.DTO.PublicUserCreateRequestDTO;
import br.com.backend.DTO.UserResponseDTO;
import br.com.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public UserResponseDTO register(@Valid @RequestBody PublicUserCreateRequestDTO dto) {
        return service.createPublicUser(dto);
    }

    @PutMapping("{id}")
    public UserResponseDTO updatePublicUserUsername(@PathVariable UUID id,
                                                    @Valid @RequestBody PublicUserCreateRequestDTO dto) {
        return service.updatePublicUserUsername(id, dto);
    }

    @PutMapping("{id}")
    public UserResponseDTO updatePublicUserPassword(@PathVariable UUID id,
                                                    @Valid @RequestBody PublicUserCreateRequestDTO dto) {
        return service.updatePublicUserPassword(id, dto);
    }
}
