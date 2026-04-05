package br.com.backend.controller;

import br.com.backend.dto.request.UpdatePasswordRequest;
import br.com.backend.dto.request.UpdateUsernameRequest;
import br.com.backend.dto.request.UserCreateRequest;
import br.com.backend.dto.response.UserResponseDTO;
import br.com.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @Operation(summary = "Create user")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserResponseDTO register(@Valid @RequestBody UserCreateRequest dto) {
        return service.register(dto);
    }

    @Operation(summary = "Find user by id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserResponseDTO findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "List users")
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<UserResponseDTO> getUsers(
            @Parameter(description = "Filter by partial or email")
            @RequestParam(required = false)
            String email,

            @Parameter(description = "Filter by enabled status (true or false)")
            @RequestParam(required = false)
            Boolean enabled,

            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return service.findAll(email, enabled, pageable);
    }

    @Operation(summary = "Update username")
    @PatchMapping("/{id}/username")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserResponseDTO updateUsername(@PathVariable UUID id,
                                          @Valid @RequestBody UpdateUsernameRequest dto) {
        return service.updateEmail(id, dto);
    }

    @Operation(summary = "Update password")
    @PatchMapping("/{id}/password")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserResponseDTO updatePassword(@PathVariable UUID id,
                                          @Valid @RequestBody UpdatePasswordRequest dto){
        return service.updatePassword(id, dto);
    }

    @Operation(summary = "Deactivate user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deactivateUser(@PathVariable UUID id) {
        service.deactivate(id);
    }
}
