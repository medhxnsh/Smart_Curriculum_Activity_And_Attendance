package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.academic.model;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.people.model.Faculty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "curriculum")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Curriculum {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "curriculum_id", nullable = false, updatable = false)
    private String curriculumId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_id", nullable = false)
    private Term term;
}