package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.people.repository;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.people.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacultyRepository extends JpaRepository<Faculty, String> {
    Optional<Faculty> findByUserPrimaryEmail(String email);
}