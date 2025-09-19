package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.academic.repository;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.academic.model.ClassSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassSessionRepository extends JpaRepository<ClassSession, String> {
    Optional<ClassSession> findByQrToken(String qrToken);
}