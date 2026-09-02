package com.antony.benchmate.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestTestController {
    @GetMapping("/api/student/test")
    @PreAuthorize("hasRole('STUDENT')")
    public String studentTest() {
        return "Student access granted";
    }

    @GetMapping("/api/teacher/test")
    @PreAuthorize("hasRole('TEACHER')")
    public String teacherTest() {
        return "Teacher access granted";
    }

    @GetMapping("/api/admin/test")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminTest() {
        return "Admin access granted";
    }
}
