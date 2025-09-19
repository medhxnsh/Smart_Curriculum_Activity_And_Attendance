package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.academic.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "timetable_entries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimetableEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "tt_entry_id", nullable = false, updatable = false)
    private String ttEntryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curriculum_id", nullable = false)
    private Curriculum curriculum;

    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "room_number", length = 50)
    private String roomNumber;

    @Column(name = "is_free_period", nullable = false)
    private Boolean isFreePeriod = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TimetableStatus status = TimetableStatus.DRAFT;

    public enum TimetableStatus {
        DRAFT, PUBLISHED, ARCHIVED
    }
}