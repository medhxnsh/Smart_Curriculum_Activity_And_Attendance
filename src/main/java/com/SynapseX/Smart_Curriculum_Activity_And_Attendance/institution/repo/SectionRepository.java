package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.institution.repo;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.institution.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, String> {
}