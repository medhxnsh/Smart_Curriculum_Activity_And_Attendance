package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.controller;

import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.dto.LoginRequest;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.model.UserEntity;
import com.SynapseX.Smart_Curriculum_Activity_And_Attendance.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserEntity> registerUser(@RequestBody LoginRequest dto) {
        return ResponseEntity.ok(userService.registerUser(dto));
    }
}