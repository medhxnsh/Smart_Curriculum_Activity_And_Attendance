package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.repository;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.model.AuthCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthCredentialRepository extends JpaRepository<AuthCredential, String> {
    Optional<AuthCredential> findByUserUserIdAndAuthType(String userId, String authType);
    Optional<AuthCredential> findByUserPrimaryEmailAndAuthType(String email, String authType);
}