package br.com.backend.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public class PublicUserCreateRequestDTO {

    @Getter
    @Setter
    @NotBlank(message = "Username is required")
    private String username;

    @Getter
    @Setter
    @NotBlank(message = "password is required")
    private String password;
}
