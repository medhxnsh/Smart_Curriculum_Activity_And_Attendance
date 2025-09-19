package com.SynapseX.Smart_Curriculum_Activity_And_Attendance.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping ("/hello")
    ResponseEntity<?> Hello () {
        return ResponseEntity.ok("hello from medhansh");
    }
}
