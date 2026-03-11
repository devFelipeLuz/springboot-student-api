package br.com.backend.controller;

import br.com.backend.dto.request.UserCreateRequestDTO;
import br.com.backend.dto.response.UserResponseDTO;
import br.com.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @Operation(summary = "Registra User")
    @PostMapping
    public UserResponseDTO register(@Valid @RequestBody UserCreateRequestDTO dto) {
        return service.register(dto);
    }

    @Operation(summary = "Busca Users e retorna em páginas")
    @GetMapping
    public Page<UserResponseDTO> getUsers(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return service.findAll(pageable);
    }

    @Operation(summary = "Busca Users habilitados e retorna em páginas")
    @GetMapping("/enabled")
    public Page<UserResponseDTO> getEnabledUsers(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return service.findAllEnabled(pageable);
    }

    @Operation(summary = "Busca User por ID")
    @GetMapping("/{id}")
    public UserResponseDTO findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "Atualiza username de User encontrado por ID")
    @PutMapping("/{id}")
    public UserResponseDTO updateUsername(@PathVariable UUID id,
                                          @Valid @RequestBody UserCreateRequestDTO dto) {
        return service.updateEmail(id, dto);
    }

    @Operation(summary = "Atualiza password User encontrado por ID")
    @PutMapping("/{id}")
    public UserResponseDTO updatePassword(@PathVariable UUID id,
                                          @Valid @RequestBody UserCreateRequestDTO dto){
        return service.updatePassword(id, dto);
    }

    @Operation(summary = "Deleta User encontrado por ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
