package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.service;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.dto.SmartActivityDto;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.dto.SmartActivityTypeDto;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.dto.SmartRecommendationDto;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.mapper.SmartActivityMapper;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.mapper.SmartActivityTypeMapper;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.mapper.SmartRecommendationMapper;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.model.SmartActivity;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.model.SmartActivityType;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.model.SmartRecommendation;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.repository.SmartActivityRepository;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.repository.SmartActivityTypeRepository;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.repository.SmartRecommendationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SmartService {

    private final SmartActivityRepository activityRepository;
    private final SmartActivityTypeRepository activityTypeRepository;
    private final SmartRecommendationRepository recommendationRepository;
    private final SmartActivityMapper activityMapper;
    private final SmartActivityTypeMapper activityTypeMapper;
    private final SmartRecommendationMapper recommendationMapper;

    public SmartService(SmartActivityRepository activityRepository,
                        SmartActivityTypeRepository activityTypeRepository,
                        SmartRecommendationRepository recommendationRepository,
                        SmartActivityMapper activityMapper,
                        SmartActivityTypeMapper activityTypeMapper,
                        SmartRecommendationMapper recommendationMapper) {
        this.activityRepository = activityRepository;
        this.activityTypeRepository = activityTypeRepository;
        this.recommendationRepository = recommendationRepository;
        this.activityMapper = activityMapper;
        this.activityTypeMapper = activityTypeMapper;
        this.recommendationMapper = recommendationMapper;
    }

    // Smart Activity methods
    public List<SmartActivityDto> getAllActivities() {
        return activityRepository.findAll().stream()
                .map(activityMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<SmartActivityDto> getActivityById(String id) {
        return activityRepository.findById(id).map(activityMapper::toDto);
    }

    public SmartActivityDto createActivity(SmartActivityDto dto) {
        SmartActivity entity = activityMapper.toEntity(dto);
        return activityMapper.toDto(activityRepository.save(entity));
    }

    // Smart Activity Type methods
    public List<SmartActivityTypeDto> getAllActivityTypes() {
        return activityTypeRepository.findAll().stream()
                .map(activityTypeMapper::toDto)
                .collect(Collectors.toList());
    }

    public SmartActivityTypeDto createActivityType(SmartActivityTypeDto dto) {
        SmartActivityType entity = activityTypeMapper.toEntity(dto);
        return activityTypeMapper.toDto(activityTypeRepository.save(entity));
    }

    // Smart Recommendation methods
    public List<SmartRecommendationDto> getAllRecommendations() {
        return recommendationRepository.findAll().stream()
                .map(recommendationMapper::toDto)
                .collect(Collectors.toList());
    }

    public SmartRecommendationDto createRecommendation(SmartRecommendationDto dto) {
        SmartRecommendation entity = recommendationMapper.toEntity(dto);
        return recommendationMapper.toDto(recommendationRepository.save(entity));
    }
}