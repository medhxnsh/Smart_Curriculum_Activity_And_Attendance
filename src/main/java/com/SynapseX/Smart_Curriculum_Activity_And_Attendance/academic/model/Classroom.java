package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.academic.model;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.core.model.Campus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "classrooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "classroom_id", nullable = false, updatable = false)
    private String classroomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campus_id", nullable = false)
    private Campus campus;

    @Column(name = "building", length = 255)
    private String building;

    @Column(name = "room", length = 64)
    private String room;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata;
}