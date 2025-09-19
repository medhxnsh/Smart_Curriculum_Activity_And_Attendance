package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.institution.dto;

import lombok.*;

import java.time.LocalTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeeklyScheduleRequest {

    /**
     * Map keys are day names: MONDAY, TUESDAY, ... SUNDAY (case-insensitive)
     * Value is DaySchedule.
     */
    private Map<String, DaySchedule> days;

    /**
     * If true, existing schedules for the institution will be deleted before creating new ones.
     */
    private boolean overwrite = true;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DaySchedule {
        private LocalTime openingTime;
        private LocalTime closingTime;
        private Boolean isHoliday;
    }
}