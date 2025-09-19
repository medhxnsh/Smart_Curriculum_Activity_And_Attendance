package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.attendance.controller;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.attendance.dto.AttendanceDto;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.attendance.dto.QrAttendanceRequest;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/qr-scan")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<AttendanceDto> markAttendanceByQr(@RequestBody QrAttendanceRequest request) {
        return ResponseEntity.ok(attendanceService.markAttendanceByQr(request));
    }

    @GetMapping("/session/{sessionId}")
    @PreAuthorize("hasRole('FACULTY') or hasRole('HOD') or hasRole('INSTITUTION_ADMIN')")
    public ResponseEntity<List<AttendanceDto>> getSessionAttendance(@PathVariable String sessionId) {
        return ResponseEntity.ok(attendanceService.getSessionAttendance(sessionId));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('FACULTY') or hasRole('HOD')")
    public ResponseEntity<List<AttendanceDto>> getStudentAttendance(@PathVariable String studentId) {
        return ResponseEntity.ok(attendanceService.getStudentAttendance(studentId));
    }
}