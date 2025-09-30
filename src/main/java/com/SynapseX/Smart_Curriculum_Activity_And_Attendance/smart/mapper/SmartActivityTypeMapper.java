package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.mapper;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.dto.SmartActivityTypeDto;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.model.SmartActivityType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SmartActivityTypeMapper {

    SmartActivityTypeMapper INSTANCE = Mappers.getMapper(SmartActivityTypeMapper.class);

    SmartActivityTypeDto toDto(SmartActivityType smartActivityType);

    SmartActivityType toEntity(SmartActivityTypeDto smartActivityTypeDto);
}