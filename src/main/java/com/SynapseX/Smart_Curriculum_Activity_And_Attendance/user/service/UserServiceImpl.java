package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.service;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.dto.LoginRequest;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.model.UserEntity;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserEntity registerUser(LoginRequest dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        UserEntity user = UserEntity.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .roles(Set.of("ROLE_USER")) // default role
                .active(true)
                .build();

        return userRepository.save(user);
    }
}