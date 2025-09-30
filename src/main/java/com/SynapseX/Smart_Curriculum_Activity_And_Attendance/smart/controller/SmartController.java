package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.controller;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.dto.SmartActivityDto;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.dto.SmartActivityTypeDto;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.dto.SmartRecommendationDto;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.service.SmartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/smart")
public class SmartController {

    private final SmartService smartService;

    public SmartController(SmartService smartService) {
        this.smartService = smartService;
    }

    @GetMapping("/activities")
    public ResponseEntity<List<SmartActivityDto>> getAllActivities() {
        return ResponseEntity.ok(smartService.getAllActivities());
    }

    @PostMapping("/activities")
    public ResponseEntity<SmartActivityDto> createActivity(@RequestBody SmartActivityDto dto) {
        return ResponseEntity.ok(smartService.createActivity(dto));
    }

    @GetMapping("/activity-types")
    public ResponseEntity<List<SmartActivityTypeDto>> getAllActivityTypes() {
        return ResponseEntity.ok(smartService.getAllActivityTypes());
    }

    @PostMapping("/activity-types")
    public ResponseEntity<SmartActivityTypeDto> createActivityType(@RequestBody SmartActivityTypeDto dto) {
        return ResponseEntity.ok(smartService.createActivityType(dto));
    }

    @GetMapping("/recommendations")
    public ResponseEntity<List<SmartRecommendationDto>> getAllRecommendations() {
        return ResponseEntity.ok(smartService.getAllRecommendations());
    }

    @PostMapping("/recommendations")
    public ResponseEntity<SmartRecommendationDto> createRecommendation(@RequestBody SmartRecommendationDto dto) {
        return ResponseEntity.ok(smartService.createRecommendation(dto));
    }
}