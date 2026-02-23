package br.com.backend.domain;

import br.com.backend.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Getter
    private String username;

    @Getter
    private String password;

    @Getter
    @Enumerated(EnumType.STRING)
    private Role role;

    @Getter
    private Boolean enabled;

    @JsonIgnore
    @Getter
    private LocalDate createdAt;

    protected User() {}

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.enabled = true;
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

    public enum Role {
        USER,
        ADMIN;

        public String getAuthority() {
            return "ROLE_" + this.name();
        }
    }

    public static User createUser(
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
    }
}
