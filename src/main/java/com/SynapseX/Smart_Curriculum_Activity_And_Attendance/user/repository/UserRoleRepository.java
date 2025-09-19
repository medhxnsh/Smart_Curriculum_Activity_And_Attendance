package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.repository;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, String> {
    List<UserRole> findByUserUserId(String userId);
}