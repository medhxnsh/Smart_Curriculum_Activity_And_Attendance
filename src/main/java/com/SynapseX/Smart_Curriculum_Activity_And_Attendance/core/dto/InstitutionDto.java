package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.core.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class InstitutionDto {
    private String institutionId;
    private String groupId;
    private String name;
    private String address;
    private String affiliatingBoard;
    private String ownerName;
    private String contact;
    private String createdBy;
    private Boolean affiliationLocked;
    private Boolean isActive;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}