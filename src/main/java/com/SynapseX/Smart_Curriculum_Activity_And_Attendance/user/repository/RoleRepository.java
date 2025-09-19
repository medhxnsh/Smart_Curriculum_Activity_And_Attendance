package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.repository;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByKeyName(String keyName);
}