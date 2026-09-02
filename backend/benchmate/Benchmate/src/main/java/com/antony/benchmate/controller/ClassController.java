package com.antony.benchmate.controller;

import com.antony.benchmate.entity.ClassEntity;
import com.antony.benchmate.repository.ClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost")
public class ClassController {

    private final ClassRepository classRepository;

    @GetMapping("/{classId}")
    public ResponseEntity<ClassEntity> getClassById(
            @PathVariable Integer classId) {

        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() ->
                        new RuntimeException("Class not found"));

        return ResponseEntity.ok(classEntity);
    }
}