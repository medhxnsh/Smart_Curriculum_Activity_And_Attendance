package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.institution.service.impl;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.institution.dto.InstitutionScheduleDto;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.institution.dto.WeeklyScheduleRequest;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.institution.model.Institution;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.institution.model.InstitutionSchedule;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.institution.repo.InstitutionRepository;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.institution.repo.InstitutionScheduleRepository;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.institution.service.InstitutionScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstitutionScheduleServiceImpl implements InstitutionScheduleService {

    private final InstitutionRepository institutionRepository;
    private final InstitutionScheduleRepository scheduleRepository;

    @Override
    public InstitutionScheduleDto createSchedule(InstitutionScheduleDto dto) {
        Institution institution = institutionRepository.findById(dto.getInstitutionId())
                .orElseThrow(() -> new RuntimeException("Institution not found"));

        InstitutionSchedule schedule = InstitutionSchedule.builder()
                .institution(institution)
                .dayOfWeek(dto.getDayOfWeek())
                .openingTime(dto.getOpeningTime())
                .closingTime(dto.getClosingTime())
                .isHoliday(dto.getIsHoliday() != null ? dto.getIsHoliday() : false)
                .build();

        InstitutionSchedule saved = scheduleRepository.save(schedule);

        dto.setScheduleId(saved.getScheduleId());
        return dto;
    }

    @Override
    public List<InstitutionScheduleDto> getSchedulesForInstitution(String institutionId) {
        return scheduleRepository.findByInstitution_InstitutionId(institutionId).stream()
                .map(s -> InstitutionScheduleDto.builder()
                        .scheduleId(s.getScheduleId())
                        .institutionId(s.getInstitution().getInstitutionId())
                        .dayOfWeek(s.getDayOfWeek())
                        .openingTime(s.getOpeningTime())
                        .closingTime(s.getClosingTime())
                        .isHoliday(s.getIsHoliday())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<InstitutionScheduleDto> generateWeeklySchedule(String institutionId, WeeklyScheduleRequest req) {
        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new RuntimeException("Institution not found"));

        if (req.isOverwrite()) {
            List<InstitutionSchedule> existing = scheduleRepository.findByInstitution_InstitutionId(institutionId);
            if (!existing.isEmpty()) scheduleRepository.deleteAll(existing);
        }

        Map<String, WeeklyScheduleRequest.DaySchedule> provided = Optional.ofNullable(req.getDays())
                .orElse(Collections.emptyMap()).entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().toUpperCase(Locale.ROOT), Map.Entry::getValue));

        List<InstitutionSchedule> toSave = new ArrayList<>(7);
        for (DayOfWeek dow : DayOfWeek.values()) {
            String key = dow.name(); // MONDAY ..
            WeeklyScheduleRequest.DaySchedule ds = provided.get(key);

            InstitutionSchedule.InstitutionScheduleBuilder builder = InstitutionSchedule.builder()
                    .institution(institution)
                    .dayOfWeek(key);

            if (ds != null) {
                Boolean isHoliday = ds.getIsHoliday() != null ? ds.getIsHoliday() : (ds.getOpeningTime() == null && ds.getClosingTime() == null);
                builder.openingTime(ds.getOpeningTime());
                builder.closingTime(ds.getClosingTime());
                builder.isHoliday(isHoliday);
            } else {
                // defaults: Mon-Fri 08:00-16:00, Sat 08:00-12:00, Sun holiday
                switch (dow) {
                    case SATURDAY -> {
                        builder.openingTime(LocalTime.of(8, 0));
                        builder.closingTime(LocalTime.of(12, 0));
                        builder.isHoliday(false);
                    }
                    case SUNDAY -> {
                        builder.openingTime(null);
                        builder.closingTime(null);
                        builder.isHoliday(true);
                    }
                    default -> {
                        builder.openingTime(LocalTime.of(8, 0));
                        builder.closingTime(LocalTime.of(16, 0));
                        builder.isHoliday(false);
                    }
                }
            }
            toSave.add(builder.build());
        }

        List<InstitutionSchedule> saved = scheduleRepository.saveAll(toSave);

        return saved.stream().map(s -> InstitutionScheduleDto.builder()
                .scheduleId(s.getScheduleId())
                .institutionId(s.getInstitution().getInstitutionId())
                .dayOfWeek(s.getDayOfWeek())
                .openingTime(s.getOpeningTime())
                .closingTime(s.getClosingTime())
                .isHoliday(s.getIsHoliday())
                .build()).collect(Collectors.toList());
    }
}