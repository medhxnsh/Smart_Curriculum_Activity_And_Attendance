package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.people.model;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "faculty")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Faculty {

    @Id
    @Column(name = "faculty_id", nullable = false, updatable = false)
    private String facultyId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "faculty_id")
    private UserEntity user;

    @Column(name = "employee_id", length = 64)
    private String employeeId;

    @Column(name = "joining_date")
    private java.time.LocalDate joiningDate;

    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata; // store JSON string; change to Map if needed
}