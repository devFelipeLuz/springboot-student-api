package br.com.backend.service;

import br.com.backend.domain.User;
import br.com.backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository repository;

    public CustomUserDetailService(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
    }

    @Override
    public User loadUserByUsername(String username) {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + "não encontrado"));


        return user;
    }
}
