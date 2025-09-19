package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.institution.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sections",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"batch_id","name"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "section_id", nullable = false, updatable = false)
    private String sectionId;

    @Column(name = "batch_id", nullable = false)
    private String batchId; // simple reference, keep as String (FK relationship can be added later)

    @Column(name = "name", nullable = false, length = 10)
    private String name;
}