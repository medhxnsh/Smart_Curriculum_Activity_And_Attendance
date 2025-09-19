package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.core.repository;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.core.model.Institution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstitutionRepository extends JpaRepository<Institution, String> {
}