package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.people.model;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.academic.model.Section;
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

    @Id
    @Column(name = "student_id", nullable = false, updatable = false)
    private String studentId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "student_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @Column(name = "roll_number", nullable = false, length = 64)
    private String rollNumber;

    @Column(name = "admission_year")
    private Integer admissionYear;

    @Column(name = "interests", columnDefinition = "text[]")
    private String[] interests;

    @Column(name = "career_goal", columnDefinition = "text")
    private String careerGoal;
}