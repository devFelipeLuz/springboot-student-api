package br.com.backend.controller;

import br.com.backend.DTO.LoginRequest;
import br.com.backend.DTO.LoginResponse;
import br.com.backend.domain.User;
import br.com.backend.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager manager;
    private final JwtService jwtService;

    public AuthenticationController(AuthenticationManager manager, JwtService jwtService) {
        this.manager = manager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                request.username(),
                request.password());

        Authentication authentication =  manager.authenticate(authToken);
        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(user);

        return new LoginResponse(token);
    }
}
