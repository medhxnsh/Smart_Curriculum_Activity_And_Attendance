package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.service;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.dto.LoginRequest;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.model.UserEntity;

public interface UserService {
    UserEntity registerUser(LoginRequest dto);
}