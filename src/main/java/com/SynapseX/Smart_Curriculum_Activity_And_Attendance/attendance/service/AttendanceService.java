package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.attendance.service;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.attendance.dto.AttendanceDto;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.attendance.dto.QrAttendanceRequest;

import java.util.List;

public interface AttendanceService {
    AttendanceDto markAttendanceByQr(QrAttendanceRequest request);
    List<AttendanceDto> getSessionAttendance(String sessionId);
    List<AttendanceDto> getStudentAttendance(String studentId);
}