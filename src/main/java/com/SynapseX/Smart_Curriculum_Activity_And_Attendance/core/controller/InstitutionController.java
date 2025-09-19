package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.core.controller;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.core.dto.InstitutionDto;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.core.service.InstitutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/institutions")
@RequiredArgsConstructor
public class InstitutionController {

    private final InstitutionService institutionService;

    @PostMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseEntity<InstitutionDto> createInstitution(@RequestBody InstitutionDto dto) {
        return ResponseEntity.ok(institutionService.createInstitution(dto));
    }

    @GetMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<List<InstitutionDto>> getAllInstitutions() {
        return ResponseEntity.ok(institutionService.getAllInstitutions());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseEntity<InstitutionDto> getInstitution(@PathVariable String id) {
        return ResponseEntity.ok(institutionService.getInstitution(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseEntity<InstitutionDto> updateInstitution(@PathVariable String id, @RequestBody InstitutionDto dto) {
        return ResponseEntity.ok(institutionService.updateInstitution(id, dto));
    }
}