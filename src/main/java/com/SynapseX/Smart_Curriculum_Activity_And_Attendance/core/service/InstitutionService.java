package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.core.service;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.core.dto.InstitutionDto;

import java.util.List;

public interface InstitutionService {
    InstitutionDto createInstitution(InstitutionDto dto);
    InstitutionDto getInstitution(String institutionId);
    List<InstitutionDto> getAllInstitutions();
    InstitutionDto updateInstitution(String institutionId, InstitutionDto dto);
}