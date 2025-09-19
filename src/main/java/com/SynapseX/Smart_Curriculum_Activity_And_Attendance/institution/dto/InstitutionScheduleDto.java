package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.institution.dto;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstitutionScheduleDto {
    private String scheduleId;
    private String institutionId;
    private String dayOfWeek;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private Boolean isHoliday;
}