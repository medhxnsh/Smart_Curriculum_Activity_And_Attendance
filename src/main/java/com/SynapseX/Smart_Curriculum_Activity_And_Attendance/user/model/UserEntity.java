package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", nullable = false, updatable = false)
    private String userId;

    @Column(name = "institution_id")
    private String institutionId;

    @Column(name = "primary_email", unique = true, nullable = false, length = 320)
    private String primaryEmail;

    @Column(name = "display_name", length = 255)
    private String displayName;

    @Column(name = "phone", length = 32)
    private String phone;

    @Column(name = "profile", columnDefinition = "jsonb")
    private String profile;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @PrePersist
    public void prePersist() {
        createdAt = OffsetDateTime.now();
        if (isActive == null) isActive = true;
    }
}