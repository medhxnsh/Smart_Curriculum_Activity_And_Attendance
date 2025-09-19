package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.service;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.model.AuthCredential;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.model.UserEntity;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.model.UserRole;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.repository.AuthCredentialRepository;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.repository.UserRepository;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AuthCredentialRepository authCredentialRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByPrimaryEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        AuthCredential credential = authCredentialRepository
                .findByUserPrimaryEmailAndAuthType(email, "password")
                .orElseThrow(() -> new UsernameNotFoundException("No password credential found for user: " + email));

        List<UserRole> userRoles = userRoleRepository.findByUserUserId(user.getUserId());
        List<GrantedAuthority> authorities = userRoles.stream()
                .map(ur -> new SimpleGrantedAuthority("ROLE_" + ur.getRole().getKeyName().toUpperCase()))
                .collect(Collectors.toList());

        return new User(
                user.getPrimaryEmail(),
                credential.getPasswordHash(),
                user.getIsActive(),
                true, true, true,
                authorities
        );
    }
}