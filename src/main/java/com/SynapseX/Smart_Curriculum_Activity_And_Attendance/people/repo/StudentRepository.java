package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.people.repo;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.people.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, String> {
}