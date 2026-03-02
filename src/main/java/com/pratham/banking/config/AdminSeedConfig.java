package com.pratham.banking.config;

import com.pratham.banking.entity.Role;
import com.pratham.banking.entity.User;
import com.pratham.banking.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminSeedConfig {

    private static final Logger logger = LoggerFactory.getLogger(AdminSeedConfig.class);

    @Bean
    public CommandLineRunner seedAdminUser(UserRepository userRepository,
                                           PasswordEncoder passwordEncoder,
                                           @Value("${app.seed.admin.enabled:true}") boolean enabled,
                                           @Value("${app.seed.admin.username:admin}") String username,
                                           @Value("${app.seed.admin.password:admin123}") String password) {
        return args -> {
            if (!enabled) {
                return;
            }

            if (username == null || username.isBlank() || password == null || password.isBlank()) {
                logger.warn("Admin seeding skipped because username/password is blank");
                return;
            }

            if (userRepository.findByUsername(username).isPresent()) {
                return;
            }

            User admin = User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(admin);
            logger.info("Seeded admin user with username: {}", username);
        };
    }
}
