package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.academic.model;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.core.model.Campus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "programs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Program {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "program_id", nullable = false, updatable = false)
    private String programId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campus_id", nullable = false)
    private Campus campus;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "program_code", nullable = false, length = 20)
    private String programCode;

    @Column(name = "duration_years", nullable = false)
    private Integer durationYears;
}