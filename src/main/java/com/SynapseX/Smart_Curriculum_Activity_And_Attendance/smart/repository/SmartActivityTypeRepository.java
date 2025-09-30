package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.repository;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.model.SmartActivityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmartActivityTypeRepository extends JpaRepository<SmartActivityType, String> {
}