package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.attendance.service.impl;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.attendance.dto.AttendanceDto;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.attendance.dto.QrAttendanceRequest;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.attendance.model.AttendanceRecord;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.attendance.model.StudentDevice;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.attendance.repository.AttendanceRepository;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.attendance.repository.StudentDeviceRepository;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.attendance.service.AttendanceService;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.academic.model.ClassSession;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.academic.repository.ClassSessionRepository;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.people.model.Student;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.people.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentDeviceRepository studentDeviceRepository;
    private final ClassSessionRepository classSessionRepository;
    private final StudentRepository studentRepository;

    @Override
    @Transactional
    public AttendanceDto markAttendanceByQr(QrAttendanceRequest request) {
        // Find the class session by QR token
        ClassSession session = classSessionRepository.findByQrToken(request.getQrToken())
                .orElseThrow(() -> new RuntimeException("Invalid QR code"));

        // Check if QR code is still valid
        if (session.getQrExpiresAt().isBefore(OffsetDateTime.now())) {
            throw new RuntimeException("QR code has expired");
        }

        // Get current user (student)
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = studentRepository.findByUserPrimaryEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Verify device belongs to student
        StudentDevice device = studentDeviceRepository.findById(request.getDeviceId())
                .orElseThrow(() -> new RuntimeException("Device not found"));

        if (!device.getStudent().getStudentId().equals(student.getStudentId())) {
            throw new RuntimeException("Device does not belong to student");
        }

        // Check if attendance already marked
        if (attendanceRepository.existsBySessionAndStudent(session, student)) {
            throw new RuntimeException("Attendance already marked for this session");
        }

        // Create attendance record
        AttendanceRecord attendance = AttendanceRecord.builder()
                .session(session)
                .student(student)
                .device(device)
                .status(AttendanceRecord.AttendanceStatus.PRESENT)
                .method(AttendanceRecord.AttendanceMethod.QR_SCAN)
                .locationData(request.getLocationData())
                .wifiBssid(request.getWifiBssid())
                .bluetoothBeaconId(request.getBluetoothBeaconId())
                .confidenceScore(100) // Will be adjusted by triggers
                .build();

        AttendanceRecord saved = attendanceRepository.save(attendance);
        return mapToDto(saved);
    }

    @Override
    public List<AttendanceDto> getSessionAttendance(String sessionId) {
        ClassSession session = classSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        return attendanceRepository.findBySession(session).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceDto> getStudentAttendance(String studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return attendanceRepository.findByStudent(student).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private AttendanceDto mapToDto(AttendanceRecord attendance) {
        AttendanceDto dto = new AttendanceDto();
        dto.setAttendanceId(attendance.getAttendanceId());
        dto.setSessionId(attendance.getSession().getSessionId());
        dto.setStudentId(attendance.getStudent().getStudentId());
        dto.setDeviceId(attendance.getDevice() != null ? attendance.getDevice().getDeviceId() : null);
        dto.setStatus(attendance.getStatus());
        dto.setMarkedAt(attendance.getMarkedAt());
        dto.setMethod(attendance.getMethod());
        dto.setMarkedBy(attendance.getMarkedBy() != null ? attendance.getMarkedBy().getUserId() : null);
        dto.setLocationData(attendance.getLocationData());
        dto.setWifiBssid(attendance.getWifiBssid());
        dto.setBluetoothBeaconId(attendance.getBluetoothBeaconId());
        dto.setConfidenceScore(attendance.getConfidenceScore());
        return dto;
    }
}