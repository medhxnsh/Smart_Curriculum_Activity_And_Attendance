package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}