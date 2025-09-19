package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.service;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.dto.LoginRequest;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.model.AuthCredential;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.model.Role;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.model.UserEntity;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.model.UserRole;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.repository.AuthCredentialRepository;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.repository.RoleRepository;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.repository.UserRepository;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthCredentialRepository authCredentialRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserEntity registerUser(LoginRequest dto) {
        if (userRepository.existsByPrimaryEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Create user
        UserEntity user = UserEntity.builder()
                .primaryEmail(dto.getEmail())
                .displayName(dto.getEmail().split("@")[0]) // Use email prefix as display name
                .isActive(true)
                .build();
        user = userRepository.save(user);

        // Create auth credential
        AuthCredential credential = AuthCredential.builder()
                .user(user)
                .authType("password")
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .build();
        authCredentialRepository.save(credential);

        // Assign default role (student)
        Role studentRole = roleRepository.findByKeyName("student")
                .orElseThrow(() -> new RuntimeException("Default student role not found"));
        
        UserRole userRole = UserRole.builder()
                .user(user)
                .role(studentRole)
                .scope("{}")
                .build();
        userRoleRepository.save(userRole);

        return user;
    }
}