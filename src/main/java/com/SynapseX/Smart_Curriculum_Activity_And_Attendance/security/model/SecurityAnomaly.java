package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.security.model;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.people.model.Faculty;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.people.model.Student;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "security_anomalies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecurityAnomaly {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "anomaly_id", nullable = false, updatable = false)
    private String anomalyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "description", nullable = false, columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false)
    private Severity severity = Severity.MEDIUM;

    @Column(name = "evidence", columnDefinition = "jsonb")
    private String evidence;

    @Column(name = "resolved", nullable = false)
    private Boolean resolved = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolved_by")
    private UserEntity resolvedBy;

    @Column(name = "resolved_at")
    private OffsetDateTime resolvedAt;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @PrePersist
    private void onCreate() {
        if (createdAt == null) createdAt = OffsetDateTime.now();
        if (severity == null) severity = Severity.MEDIUM;
        if (resolved == null) resolved = false;
    }

    public enum Severity {
        LOW, MEDIUM, HIGH, CRITICAL
    }
}