package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.attendance.repository;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.attendance.model.AttendanceRecord;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.academic.model.ClassSession;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.people.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<AttendanceRecord, String> {
    List<AttendanceRecord> findBySession(ClassSession session);
    List<AttendanceRecord> findByStudent(Student student);
    boolean existsBySessionAndStudent(ClassSession session, Student student);
}