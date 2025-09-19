package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "role_id", nullable = false, updatable = false)
    private String roleId;

    @Column(name = "key_name", nullable = false, unique = true, length = 100)
    private String keyName; // e.g., system_admin, institution_admin, student, faculty

    @Column(name = "display_name", length = 120)
    private String displayName;

    @Column(name = "description", columnDefinition = "text")
    private String description;
}