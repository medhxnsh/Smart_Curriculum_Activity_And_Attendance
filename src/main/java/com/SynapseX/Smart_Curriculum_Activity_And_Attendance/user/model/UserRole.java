package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "user_roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_role_id", nullable = false, updatable = false)
    private String userRoleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "scope", columnDefinition = "jsonb", nullable = false)
    private String scope = "{}";

    @Column(name = "assigned_at")
    private OffsetDateTime assignedAt;

    @PrePersist
    private void onCreate() {
        if (assignedAt == null) assignedAt = OffsetDateTime.now();
        if (scope == null) scope = "{}";
    }
}