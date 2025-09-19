package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.institution.service;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.institution.dto.InstitutionDto;

import java.util.List;

public interface InstitutionService {
    InstitutionDto createInstitution(InstitutionDto dto);
    InstitutionDto getInstitution(String institutionId);
    List<InstitutionDto> listInstitutions();
}