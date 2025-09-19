package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.model;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.academic.model.Subject;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "smart_activities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmartActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "activity_id", nullable = false, updatable = false)
    private String activityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private SmartActivityType type;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "url", nullable = false, columnDefinition = "text")
    private String url;

    @Column(name = "expected_duration_min", nullable = false)
    private Integer expectedDurationMin;

    @Column(name = "difficulty_level", length = 20)
    private String difficultyLevel;

    @Column(name = "tags", columnDefinition = "text[]", nullable = false)
    private String[] tags = {};

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "associated_subject_id")
    private Subject associatedSubject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private UserEntity createdBy;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @PrePersist
    private void onCreate() {
        if (createdAt == null) createdAt = OffsetDateTime.now();
        if (isActive == null) isActive = true;
        if (tags == null) tags = new String[]{};
    }
}