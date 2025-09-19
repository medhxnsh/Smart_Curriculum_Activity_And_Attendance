package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.config;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.model.AuthCredential;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.model.Role;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.model.UserEntity;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.model.UserRole;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.repository.AuthCredentialRepository;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.repository.RoleRepository;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.repository.UserRepository;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final AuthCredentialRepository authCredentialRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @Transactional
    CommandLineRunner initUsers() {
        return args -> {
            if (!userRepository.existsByPrimaryEmail("admin@system.com")) {
                // Create admin user
                UserEntity admin = UserEntity.builder()
                        .primaryEmail("admin@system.com")
                        .displayName("System Administrator")
                        .isActive(true)
                        .build();
                admin = userRepository.save(admin);

                // Create auth credential
                AuthCredential credential = AuthCredential.builder()
                        .user(admin)
                        .authType("password")
                        .passwordHash(passwordEncoder.encode("password"))
                        .build();
                authCredentialRepository.save(credential);

                // Assign system admin role
                Role adminRole = roleRepository.findByKeyName("system_admin")
                        .orElseThrow(() -> new RuntimeException("System admin role not found"));
                
                UserRole userRole = UserRole.builder()
                        .user(admin)
                        .role(adminRole)
                        .scope("{}")
                        .build();
                userRoleRepository.save(userRole);

                System.out.println("✅ Default admin created: email=admin@system.com, password=password");
            } else {
                System.out.println("ℹ️ Admin user already exists, skipping seeding.");
            }
        };
    }
}