package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "smart_activity_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmartActivityType {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "type_id", nullable = false, updatable = false)
    private String typeId;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "icon", length = 50)
    private String icon;
}