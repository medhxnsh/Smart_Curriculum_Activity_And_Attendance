package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.repository;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.smart.model.SmartActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmartActivityRepository extends JpaRepository<SmartActivity, String> {
}