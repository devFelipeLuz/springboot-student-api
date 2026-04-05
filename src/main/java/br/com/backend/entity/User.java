package br.com.backend.entity;

import br.com.backend.entity.enums.Role;
import br.com.backend.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean enabled;

    @Column(updatable = false)
    private Instant deletedAt;

    @JsonIgnore
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    public User(String email, String password, Role role) {
        this.email = validateEmail(email);
        this.password = validatePassword(password);
        this.role = Objects.requireNonNull(role, "Role is null");
        this.enabled = true;
        this.deletedAt = null;
        this.createdAt = Instant.now();
    }

    public static User createUser(
            String email,
            String encodedPassword,
            Role role) {

        return new User(email, encodedPassword, role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println(">>> Autoridades do usuário:" + role.getAuthority());
        return List.of(new SimpleGrantedAuthority(role.getAuthority()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.enabled && this.deletedAt == null;
    }

    public void updateEmail(String email) {
        ensureActive();
        validateEmail(email);
        this.email = validateEmail(email);
    }

    public void updatePassword(String password) {
        ensureActive();
        validatePassword(password);
        this.password = validatePassword(password);
    }

    public void deactivate() {
        this.enabled = false;
        this.deletedAt = Instant.now();
    }

    public void ensureActive() {
        if (!this.enabled) {
            throw new BusinessException("User is not enabled");
        }
    }

    public String validateEmail(String email) {
        if (email == null || email.isBlank()) {
           throw new BusinessException("Email is null or blank");
        }

        return email;
    }

    public String validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new BusinessException("Password is null or blank");
        }

        return password;
    }
}
