package br.com.backend.repository;

import br.com.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    List<User> findAllByEnabledTrue();
    Optional<User> findByUsername(String username);
}
