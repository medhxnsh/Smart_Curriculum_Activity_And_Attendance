package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.dto;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class SmartRecommendationDto {
    private String recommendationId;
    private String studentId;
    private String activityId;
    private OffsetDateTime recommendedFor;
    private String recommendedBasis;
    private Integer priority;
    private Boolean isCompleted;
    private OffsetDateTime completedAt;
    private Integer rating;
    private String feedback;
    private OffsetDateTime createdAt;
}