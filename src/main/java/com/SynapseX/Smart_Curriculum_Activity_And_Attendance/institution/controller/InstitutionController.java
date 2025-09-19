package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.institution.controller;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.institution.dto.InstitutionDto;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.institution.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/institutions")
@RequiredArgsConstructor
public class InstitutionController {

    private final InstitutionService service;

    @PostMapping
    public ResponseEntity<InstitutionDto> create(@RequestBody InstitutionDto dto) {
        return ResponseEntity.ok(service.createInstitution(dto));
    }

    @GetMapping
    public ResponseEntity<List<InstitutionDto>> all() {
        return ResponseEntity.ok(service.listInstitutions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstitutionDto> get(@PathVariable("id") String id) {
        return ResponseEntity.ok(service.getInstitution(id));
    }
}