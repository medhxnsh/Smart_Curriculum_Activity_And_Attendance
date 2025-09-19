package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.attendance.dto;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.attendance.model.AttendanceRecord;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class AttendanceDto {
    private String attendanceId;
    private String sessionId;
    private String studentId;
    private String deviceId;
    private AttendanceRecord.AttendanceStatus status;
    private OffsetDateTime markedAt;
    private AttendanceRecord.AttendanceMethod method;
    private String markedBy;
    private String locationData;
    private String wifiBssid;
    private String bluetoothBeaconId;
    private Integer confidenceScore;
}