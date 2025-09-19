package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.attendance.repository;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.attendance.model.StudentDevice;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.people.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentDeviceRepository extends JpaRepository<StudentDevice, String> {
    List<StudentDevice> findByStudent(Student student);
    List<StudentDevice> findByStudentAndIsActive(Student student, Boolean isActive);
}