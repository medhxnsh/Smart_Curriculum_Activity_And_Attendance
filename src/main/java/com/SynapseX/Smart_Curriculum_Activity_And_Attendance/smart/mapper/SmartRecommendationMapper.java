package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.mapper;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.dto.SmartRecommendationDto;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.model.SmartRecommendation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SmartRecommendationMapper {

    SmartRecommendationMapper INSTANCE = Mappers.getMapper(SmartRecommendationMapper.class);

    @Mapping(source = "student.studentId", target = "studentId")
    @Mapping(source = "activity.activityId", target = "activityId")
    SmartRecommendationDto toDto(SmartRecommendation smartRecommendation);

    @Mapping(source = "studentId", target = "student.studentId")
    @Mapping(source = "activityId", target = "activity.activityId")
    SmartRecommendation toEntity(SmartRecommendationDto smartRecommendationDto);
}