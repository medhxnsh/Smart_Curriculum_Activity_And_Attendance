package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.dto;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class SmartActivityDto {
    private String activityId;
    private String typeId;
    private String title;
    private String description;
    private String url;
    private Integer expectedDurationMin;
    private String difficultyLevel;
    private String[] tags;
    private String associatedSubjectId;
    private String createdById;
    private OffsetDateTime createdAt;
    private Boolean isActive;
}