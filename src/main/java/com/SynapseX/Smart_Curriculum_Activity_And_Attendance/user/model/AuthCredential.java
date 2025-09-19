package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "auth_credentials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "credential_id", nullable = false, updatable = false)
    private String credentialId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "auth_type", nullable = false, length = 50)
    private String authType = "password";

    @Column(name = "password_hash", columnDefinition = "text")
    private String passwordHash;

    @Column(name = "last_login")
    private OffsetDateTime lastLogin;

    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata;
}