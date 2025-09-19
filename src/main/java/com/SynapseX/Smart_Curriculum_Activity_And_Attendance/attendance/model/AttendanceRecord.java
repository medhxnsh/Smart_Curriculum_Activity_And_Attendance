package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.attendance.model;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.academic.model.ClassSession;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.people.model.Student;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "attendance_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "attendance_id", nullable = false, updatable = false)
    private String attendanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private ClassSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id")
    private StudentDevice device;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AttendanceStatus status = AttendanceStatus.ABSENT;

    @Column(name = "marked_at")
    private OffsetDateTime markedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "method", nullable = false)
    private AttendanceMethod method;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marked_by")
    private UserEntity markedBy;

    @Column(name = "location_data", columnDefinition = "jsonb")
    private String locationData;

    @Column(name = "wifi_bssid", length = 100)
    private String wifiBssid;

    @Column(name = "bluetooth_beacon_id", length = 100)
    private String bluetoothBeaconId;

    @Column(name = "confidence_score", nullable = false)
    private Integer confidenceScore = 100;

    @PrePersist
    private void onCreate() {
        if (markedAt == null) markedAt = OffsetDateTime.now();
        if (status == null) status = AttendanceStatus.ABSENT;
        if (confidenceScore == null) confidenceScore = 100;
    }

    public enum AttendanceStatus {
        PRESENT, ABSENT, LATE, EXCUSED
    }

    public enum AttendanceMethod {
        QR_SCAN, MANUAL_BY_FACULTY, BEACON_AUTO
    }
}