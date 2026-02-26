package br.com.backend.domain;

import br.com.backend.exception.BusinessException;
import br.com.backend.security.RefreshToken;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Boolean enabled;

    private LocalDate deletedAt;

    @JsonIgnore
    private LocalDate createdAt;

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.enabled = true;
        this.deletedAt = null;
        this.createdAt = LocalDate.now();
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = null;
        this.enabled = true;
        this.deletedAt = null;
        this.createdAt = LocalDate.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getAuthority()));
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
        return Boolean.TRUE.equals(this.enabled);
    }

    public static User createGlobalUser(
            String username,
            String encodedPassword) {

        return new User(username, encodedPassword);
    }

    public static User createAdminUser(
            String username,
            String encodedPassword,
            Role role) {

        return new User(username, encodedPassword, role);
    }

    public void updateUsername(String username) {
        if (!this.enabled) {
            throw new BusinessException("Usuário desabilitado");
        }

        this.username = username;
    }

    public void updatePassword(String password) {
        if (!this.enabled) {
            throw new BusinessException("Usuário desabilitado");
        }

        this.password = password;
    }

    public void deactivate() {
        this.enabled = false;
        this.deletedAt = LocalDate.now();
    }
}
