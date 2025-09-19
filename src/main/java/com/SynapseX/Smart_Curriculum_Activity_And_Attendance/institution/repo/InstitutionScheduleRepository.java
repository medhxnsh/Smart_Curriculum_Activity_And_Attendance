package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.institution.repo;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.institution.model.InstitutionSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InstitutionScheduleRepository extends JpaRepository<InstitutionSchedule, String> {
    List<InstitutionSchedule> findByInstitution_InstitutionId(String institutionId);
}