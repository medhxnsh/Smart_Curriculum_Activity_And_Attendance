package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.institution.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class InstitutionDto {
    private String institutionId;
    private String name;
    private String address;
    private String affiliatingBoard;
    private String ownerName;
    private String contact;
    private Boolean affiliationLocked;
    private Boolean isActive;
    private String createdBy;
    private OffsetDateTime createdAt;

    // Add this if you actually need groupId
    private String groupId;
}