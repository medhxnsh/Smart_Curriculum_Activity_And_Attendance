package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.institution.service;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.institution.dto.InstitutionScheduleDto;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.institution.dto.WeeklyScheduleRequest;

import java.util.List;

public interface InstitutionScheduleService {
    InstitutionScheduleDto createSchedule(InstitutionScheduleDto dto);
    List<InstitutionScheduleDto> getSchedulesForInstitution(String institutionId);
    List<InstitutionScheduleDto> generateWeeklySchedule(String institutionId, WeeklyScheduleRequest req);
}