package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.attendance.model;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.people.model.Student;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "student_devices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "device_id", nullable = false, updatable = false)
    private String deviceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "device_name", length = 100)
    private String deviceName;

    @Column(name = "device_type", length = 50)
    private String deviceType;

    @Column(name = "device_token", unique = true, columnDefinition = "text")
    private String deviceToken;

    @Column(name = "fcm_token", columnDefinition = "text")
    private String fcmToken;

    @Column(name = "device_info", columnDefinition = "jsonb")
    private String deviceInfo;

    @Column(name = "registered_at")
    private OffsetDateTime registeredAt;

    @Column(name = "last_seen")
    private OffsetDateTime lastSeen;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @PrePersist
    private void onCreate() {
        if (registeredAt == null) registeredAt = OffsetDateTime.now();
        if (isActive == null) isActive = true;
    }
}