package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.attendance.dto;

import lombok.Data;

@Data
public class QrAttendanceRequest {
    private String qrToken;
    private String deviceId;
    private String locationData; // JSON string with GPS coordinates
    private String wifiBssid;
    private String bluetoothBeaconId;
}