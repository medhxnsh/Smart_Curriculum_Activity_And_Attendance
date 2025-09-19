package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.institution.controller;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.institution.dto.InstitutionScheduleDto;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.institution.dto.WeeklyScheduleRequest;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.institution.service.InstitutionScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/institutions/{institutionId}/schedules")
@RequiredArgsConstructor
public class InstitutionScheduleController {

    private final InstitutionScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<InstitutionScheduleDto> createSchedule(
            @PathVariable String institutionId,
            @RequestBody InstitutionScheduleDto dto) {
        dto.setInstitutionId(institutionId);
        return ResponseEntity.ok(scheduleService.createSchedule(dto));
    }

    @GetMapping
    public ResponseEntity<List<InstitutionScheduleDto>> getSchedules(@PathVariable String institutionId) {
        return ResponseEntity.ok(scheduleService.getSchedulesForInstitution(institutionId));
    }

    @PostMapping("/weekly")
    public ResponseEntity<List<InstitutionScheduleDto>> createWeekly(
            @PathVariable String institutionId,
            @RequestBody WeeklyScheduleRequest req) {
        return ResponseEntity.ok(scheduleService.generateWeeklySchedule(institutionId, req));
    }
}