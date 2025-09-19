package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.institution.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "institutions",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"group_id", "name"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Institution {

    @Id
    @Column(name = "institution_id", nullable = false, updatable = false)
    private String institutionId; // UUID as string, set by app or DB

    @Column(name = "group_id")
    private String groupId; // nullable FK to institution_groups.group_id

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "address", columnDefinition = "jsonb")
    private String address; // JSON stored as string

    @Column(name = "affiliating_board", length = 255, nullable = false)
    private String affiliatingBoard;

    @Column(name = "owner_name", length = 255)
    private String ownerName;

    @Column(name = "contact", columnDefinition = "jsonb")
    private String contact; // JSON stored as string

    @Column(name = "created_by")
    private String createdBy; // user_id of creator (UUID string)

    @Column(name = "affiliation_locked")
    private Boolean affiliationLocked = true;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at", columnDefinition = "timestamptz")
    private OffsetDateTime createdAt;

    // convenience: set defaults in pre-persist
    @PrePersist
    private void onCreate() {
        if (this.institutionId == null) this.institutionId = UUID.randomUUID().toString();
        if (this.createdAt == null) this.createdAt = OffsetDateTime.now();
        if (this.affiliationLocked == null) this.affiliationLocked = true;
        if (this.isActive == null) this.isActive = true;
    }
}