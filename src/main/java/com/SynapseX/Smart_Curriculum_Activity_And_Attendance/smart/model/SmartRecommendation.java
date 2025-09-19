package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.model;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.people.model.Student;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "smart_recommendations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmartRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "recommendation_id", nullable = false, updatable = false)
    private String recommendationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private SmartActivity activity;

    @Column(name = "recommended_for", nullable = false)
    private OffsetDateTime recommendedFor;

    @Column(name = "recommended_basis", columnDefinition = "text")
    private String recommendedBasis;

    @Column(name = "priority", nullable = false)
    private Integer priority = 1;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted = false;

    @Column(name = "completed_at")
    private OffsetDateTime completedAt;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "feedback", columnDefinition = "text")
    private String feedback;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @PrePersist
    private void onCreate() {
        if (createdAt == null) createdAt = OffsetDateTime.now();
        if (priority == null) priority = 1;
        if (isCompleted == null) isCompleted = false;
    }
}