package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.repository;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByPrimaryEmail(String email);
    boolean existsByPrimaryEmail(String email);
}