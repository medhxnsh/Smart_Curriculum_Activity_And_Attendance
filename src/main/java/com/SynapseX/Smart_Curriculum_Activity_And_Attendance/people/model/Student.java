package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.people.model;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    // student_id equals the underlying user id (shared primary key)
    @Id
    @Column(name = "student_id", nullable = false, updatable = false)
    private String studentId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "student_id")
    private UserEntity user;

    @Column(name = "roll_number", length = 64)
    private String rollNumber;

    @Column(name = "admission_year")
    private Integer admissionYear;

    @Column(name = "career_goal", columnDefinition = "text")
    private String careerGoal;
}