package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.people.repository;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.people.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, String> {
    Optional<Student> findByUserPrimaryEmail(String email);
}