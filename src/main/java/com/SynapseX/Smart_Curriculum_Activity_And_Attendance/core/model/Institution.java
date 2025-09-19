package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.core.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "institutions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Institution {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "institution_id", nullable = false, updatable = false)
    private String institutionId;

    @Column(name = "group_id")
    private String groupId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "address", columnDefinition = "jsonb")
    private String address;

    @Column(name = "affiliating_board", nullable = false, length = 255)
    private String affiliatingBoard;

    @Column(name = "owner_name", length = 255)
    private String ownerName;

    @Column(name = "contact", columnDefinition = "jsonb")
    private String contact;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "affiliation_locked", nullable = false)
    private Boolean affiliationLocked = true;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @PrePersist
    private void onCreate() {
        if (createdAt == null) createdAt = OffsetDateTime.now();
        if (updatedAt == null) updatedAt = OffsetDateTime.now();
        if (affiliationLocked == null) affiliationLocked = true;
        if (isActive == null) isActive = true;
    }

    @PreUpdate
    private void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}