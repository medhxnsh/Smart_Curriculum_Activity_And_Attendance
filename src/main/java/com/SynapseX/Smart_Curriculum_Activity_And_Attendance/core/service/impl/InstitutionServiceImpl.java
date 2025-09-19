package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.core.service.impl;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.core.dto.InstitutionDto;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.core.model.Institution;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.core.repository.InstitutionRepository;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.core.service.InstitutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstitutionServiceImpl implements InstitutionService {

    private final InstitutionRepository institutionRepository;

    @Override
    public InstitutionDto createInstitution(InstitutionDto dto) {
        Institution institution = Institution.builder()
                .groupId(dto.getGroupId())
                .name(dto.getName())
                .address(dto.getAddress())
                .affiliatingBoard(dto.getAffiliatingBoard())
                .ownerName(dto.getOwnerName())
                .contact(dto.getContact())
                .createdBy(dto.getCreatedBy())
                .affiliationLocked(dto.getAffiliationLocked())
                .isActive(dto.getIsActive())
                .build();

        Institution saved = institutionRepository.save(institution);
        return mapToDto(saved);
    }

    @Override
    public InstitutionDto getInstitution(String institutionId) {
        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new RuntimeException("Institution not found: " + institutionId));
        return mapToDto(institution);
    }

    @Override
    public List<InstitutionDto> getAllInstitutions() {
        return institutionRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public InstitutionDto updateInstitution(String institutionId, InstitutionDto dto) {
        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new RuntimeException("Institution not found: " + institutionId));

        institution.setName(dto.getName());
        institution.setAddress(dto.getAddress());
        institution.setAffiliatingBoard(dto.getAffiliatingBoard());
        institution.setOwnerName(dto.getOwnerName());
        institution.setContact(dto.getContact());
        institution.setAffiliationLocked(dto.getAffiliationLocked());
        institution.setIsActive(dto.getIsActive());

        Institution saved = institutionRepository.save(institution);
        return mapToDto(saved);
    }

    private InstitutionDto mapToDto(Institution institution) {
        InstitutionDto dto = new InstitutionDto();
        dto.setInstitutionId(institution.getInstitutionId());
        dto.setGroupId(institution.getGroupId());
        dto.setName(institution.getName());
        dto.setAddress(institution.getAddress());
        dto.setAffiliatingBoard(institution.getAffiliatingBoard());
        dto.setOwnerName(institution.getOwnerName());
        dto.setContact(institution.getContact());
        dto.setCreatedBy(institution.getCreatedBy());
        dto.setAffiliationLocked(institution.getAffiliationLocked());
        dto.setIsActive(institution.getIsActive());
        dto.setCreatedAt(institution.getCreatedAt());
        dto.setUpdatedAt(institution.getUpdatedAt());
        return dto;
    }
}