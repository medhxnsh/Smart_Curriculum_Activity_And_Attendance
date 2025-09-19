package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.config;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.model.UserEntity;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initUsers() {
        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                UserEntity admin = UserEntity.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("password"))
                        .roles(Set.of("ROLE_ADMIN"))
                        .active(true)
                        .build();
                userRepository.save(admin);
                System.out.println("✅ Default admin created: username=admin, password=password");
            } else {
                System.out.println("ℹ️ Admin user already exists, skipping seeding.");
            }
        };
    }
}