package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.mapper;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.dto.SmartActivityDto;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.model.SmartActivity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SmartActivityMapper {

    SmartActivityMapper INSTANCE = Mappers.getMapper(SmartActivityMapper.class);

    @Mapping(source = "type.typeId", target = "typeId")
    @Mapping(source = "associatedSubject.subjectId", target = "associatedSubjectId")
    @Mapping(source = "createdBy.userId", target = "createdById")
    SmartActivityDto toDto(SmartActivity smartActivity);

    @Mapping(source = "typeId", target = "type.typeId")
    @Mapping(source = "associatedSubjectId", target = "associatedSubject.subjectId")
    @Mapping(source = "createdById", target = "createdBy.userId")
    SmartActivity toEntity(SmartActivityDto smartActivityDto);
}