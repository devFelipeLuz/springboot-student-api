package br.com.backend.config;

import br.com.backend.entity.User;
import br.com.backend.entity.enums.Role;
import br.com.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            String adminEmail = "admin@admin.com";
            String adminPassword = "admin";

            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                User user = User.createUser(
                        adminEmail, encoder.encode(adminPassword), Role.ADMIN);
                userRepository.save(user);
                System.out.println(">>> Usuário MASTER criado com sucesso!");
            }

            String professorEmail = "professor@school.com";
            String professorPassword = "professor";

            if (userRepository.findByEmail(professorEmail).isEmpty()) {
                User professorUser = User.createUser(
                        professorEmail, encoder.encode(professorPassword), Role.PROFESSOR);
                userRepository.save(professorUser);
                System.out.println(">>> Usuário PROFESSOR criado com sucesso!");
            }

            String studentEmail = "student@school.com";
            String studentPassword = "student";

            if (userRepository.findByEmail(studentEmail).isEmpty()) {
                User studentUser = User.createUser(
                        studentEmail, encoder.encode(studentPassword), Role.STUDENT);
                userRepository.save(studentUser);
                System.out.println(">>> Usuário STUDENT criado com sucesso!");
            }
        };
    }
}
