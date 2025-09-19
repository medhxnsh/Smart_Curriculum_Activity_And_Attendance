package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.people.repo;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.people.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacultyRepository extends JpaRepository<Faculty, String> {
}