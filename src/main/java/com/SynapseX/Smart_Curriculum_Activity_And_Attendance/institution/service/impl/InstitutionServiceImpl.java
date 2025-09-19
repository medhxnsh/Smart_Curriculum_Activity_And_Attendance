package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.institution.service.impl;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.institution.dto.InstitutionDto;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.institution.model.Institution;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.institution.repo.InstitutionRepository;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.institution.service.InstitutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstitutionServiceImpl implements InstitutionService {

    private final InstitutionRepository repo;

    @Override
    public InstitutionDto createInstitution(InstitutionDto dto) {
        Institution entity = Institution.builder()
                .groupId(dto.getGroupId())
                .name(dto.getName())
                .address(dto.getAddress())
                .affiliatingBoard(dto.getAffiliatingBoard())
                .ownerName(dto.getOwnerName())
                .contact(dto.getContact())
                .createdBy(dto.getCreatedBy())
                .affiliationLocked(dto.getAffiliationLocked() != null ? dto.getAffiliationLocked() : true)
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .build();
        Institution saved = repo.save(entity);

        InstitutionDto out = new InstitutionDto();
        out.setInstitutionId(saved.getInstitutionId());
        out.setGroupId(saved.getGroupId());
        out.setName(saved.getName());
        out.setAddress(saved.getAddress());
        out.setAffiliatingBoard(saved.getAffiliatingBoard());
        out.setOwnerName(saved.getOwnerName());
        out.setContact(saved.getContact());
        out.setAffiliationLocked(saved.getAffiliationLocked());
        out.setIsActive(saved.getIsActive());
        out.setCreatedAt(saved.getCreatedAt());
        out.setCreatedBy(saved.getCreatedBy());
        return out;
    }

    @Override
    public InstitutionDto getInstitution(String institutionId) {
        Institution e = repo.findById(institutionId)
                .orElseThrow(() -> new RuntimeException("Institution not found: " + institutionId));
        InstitutionDto d = new InstitutionDto();
        d.setInstitutionId(e.getInstitutionId());
        d.setGroupId(e.getGroupId());
        d.setName(e.getName());
        d.setAddress(e.getAddress());
        d.setAffiliatingBoard(e.getAffiliatingBoard());
        d.setOwnerName(e.getOwnerName());
        d.setContact(e.getContact());
        d.setAffiliationLocked(e.getAffiliationLocked());
        d.setIsActive(e.getIsActive());
        d.setCreatedAt(e.getCreatedAt());
        d.setCreatedBy(e.getCreatedBy());
        return d;
    }

    @Override
    public List<InstitutionDto> listInstitutions() {
        return repo.findAll().stream().map(e -> {
            InstitutionDto d = new InstitutionDto();
            d.setInstitutionId(e.getInstitutionId());
            d.setGroupId(e.getGroupId());
            d.setName(e.getName());
            d.setAddress(e.getAddress());
            d.setAffiliatingBoard(e.getAffiliatingBoard());
            d.setOwnerName(e.getOwnerName());
            d.setContact(e.getContact());
            d.setAffiliationLocked(e.getAffiliationLocked());
            d.setIsActive(e.getIsActive());
            d.setCreatedAt(e.getCreatedAt());
            d.setCreatedBy(e.getCreatedBy());
            return d;
        }).collect(Collectors.toList());
    }
}