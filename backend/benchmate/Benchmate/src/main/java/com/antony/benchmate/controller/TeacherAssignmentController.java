package com.antony.benchmate.controller;

import com.antony.benchmate.dto.request.CreateTeacherAssignmentRequest;
import com.antony.benchmate.dto.response.TeacherAssignmentResponse;
import com.antony.benchmate.service.TeacherAccessService;
import com.antony.benchmate.service.TeacherAssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teacher-assignments")
@RequiredArgsConstructor
public class TeacherAssignmentController {

    private final TeacherAssignmentService teacherAssignmentService;
    private final TeacherAccessService teacherAccessService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeacherAssignmentResponse> create(
            @Valid @RequestBody CreateTeacherAssignmentRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(teacherAssignmentService.create(request));
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TeacherAssignmentResponse>> getAll() {

        return ResponseEntity.ok(
                teacherAssignmentService.getAll()
        );
    }
    @GetMapping("/{assignmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeacherAssignmentResponse> getById(
            @PathVariable Integer assignmentId) {

        return ResponseEntity.ok(
                teacherAssignmentService.getById(assignmentId)
        );
    }
    @DeleteMapping("/{assignmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> delete(
            @PathVariable Integer assignmentId) {

        teacherAssignmentService.delete(assignmentId);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Teacher assignment deleted successfully"
                )
        );
    }
    @GetMapping("/my")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<TeacherAssignmentResponse>> getMyAssignments() {

        return ResponseEntity.ok(
                teacherAssignmentService.getMyAssignments()
        );
    }
    @GetMapping("/filter")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TeacherAssignmentResponse>> filter(
            @RequestParam(required = false) Integer teacherId,
            @RequestParam(required = false) Integer classId,
            @RequestParam(required = false) Integer subjectId) {

        return ResponseEntity.ok(
                teacherAssignmentService.filter(
                        teacherId,
                        classId,
                        subjectId
                )
        );
    }


}