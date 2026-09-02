 package com.antony.benchmate.controller;

import com.antony.benchmate.dto.request.AssignmentRequest;
import com.antony.benchmate.dto.response.AssignmentResponse;
import com.antony.benchmate.entity.Assignment;
import com.antony.benchmate.entity.ClassEntity;
import com.antony.benchmate.entity.Subject;
import com.antony.benchmate.service.AssignmentService;
import com.antony.benchmate.service.FileStorageService;

import lombok.RequiredArgsConstructor;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final FileStorageService fileStorageService;


    // =====================================================
    // CREATE ASSIGNMENT
    // =====================================================

    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'REP', 'ADMIN')")
    public ResponseEntity<AssignmentResponse> createAssignment(
            @RequestBody AssignmentRequest request) {

        Assignment assignment = new Assignment();

        assignment.setTitle(request.getTitle());

        assignment.setDescription(
                request.getDescription()
        );

        assignment.setDueDate(
                request.getDueDate()
        );


        ClassEntity classEntity =
                new ClassEntity();

        classEntity.setClassId(
                request.getClassId()
        );


        Subject subject =
                new Subject();

        subject.setSubjectId(
                request.getSubjectId()
        );


        assignment.setClassEntity(
                classEntity
        );

        assignment.setSubject(
                subject
        );


        return ResponseEntity.ok(
                assignmentService.createAssignment(
                        assignment
                )
        );
    }


    // =====================================================
    // UPLOAD ASSIGNMENT
    // =====================================================

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('TEACHER', 'REP', 'ADMIN')")
    public ResponseEntity<AssignmentResponse> uploadAssignment(

            @RequestParam("title")
            String title,

            @RequestParam("description")
            String description,

            @RequestParam("classId")
            Integer classId,

            @RequestParam("subjectId")
            Integer subjectId,

            @RequestParam("dueDate")
            String dueDate,

            @RequestParam("file")
            MultipartFile file) {

        Assignment assignment = new Assignment();

        assignment.setTitle(title);
        assignment.setDescription(description);

        assignment.setDueDate(
                java.time.LocalDateTime.parse(dueDate)
        );

        ClassEntity classEntity = new ClassEntity();
        classEntity.setClassId(classId);

        Subject subject = new Subject();
        subject.setSubjectId(subjectId);

        assignment.setClassEntity(classEntity);
        assignment.setSubject(subject);

        /*
         * Authorization and validation happen
         * inside AssignmentService before
         * the physical file is stored.
         */
        assignmentService.validateAssignmentAccess(
                assignment
        );

        String fileName = null;

        try {

            fileName = fileStorageService.storeFile(file);

            assignment.setFileUrl(
                    "/uploads/notes/" + fileName
            );

            return ResponseEntity.ok(
                    assignmentService.createAssignment(
                            assignment
                    )
            );

        } catch (RuntimeException e) {

            if (fileName != null) {
                fileStorageService.deleteFile(fileName);
            }

            throw e;
        }
    }




    // =====================================================
    // GET ALL ASSIGNMENTS
    // =====================================================

    @GetMapping
    @PreAuthorize(
            "hasAnyRole('STUDENT', 'REP', 'TEACHER', 'ADMIN')"
    )
    public ResponseEntity<List<AssignmentResponse>>
    getAllAssignments() {

        return ResponseEntity.ok(
                assignmentService.getAllAssignments()
        );
    }


    // =====================================================
    // GET ASSIGNMENT BY ID
    // =====================================================

    @GetMapping("/{assignmentId}")
    @PreAuthorize(
            "hasAnyRole('STUDENT', 'REP', 'TEACHER', 'ADMIN')"
    )
    public ResponseEntity<AssignmentResponse>
    getAssignmentById(
            @PathVariable Integer assignmentId) {

        return ResponseEntity.ok(
                assignmentService.getAssignmentById(
                        assignmentId
                )
        );
    }


    // =====================================================
    // GET ASSIGNMENTS BY CLASS
    // =====================================================

    @GetMapping("/class/{classId}")
    @PreAuthorize(
            "hasAnyRole('STUDENT', 'REP', 'TEACHER', 'ADMIN')"
    )
    public ResponseEntity<List<AssignmentResponse>>
    getAssignmentsByClass(
            @PathVariable Integer classId) {

        return ResponseEntity.ok(
                assignmentService.getAssignmentsByClass(
                        classId
                )
        );
    }


    // =====================================================
    // GET ASSIGNMENTS BY CLASS + SUBJECT
    // =====================================================

    @GetMapping("/class/{classId}/subject/{subjectId}")
    @PreAuthorize(
            "hasAnyRole('STUDENT', 'REP', 'TEACHER', 'ADMIN')"
    )
    public ResponseEntity<List<AssignmentResponse>>
    getAssignmentsByClassAndSubject(

            @PathVariable Integer classId,

            @PathVariable Integer subjectId) {

        return ResponseEntity.ok(
                assignmentService
                        .getAssignmentsByClassAndSubject(
                                classId,
                                subjectId
                        )
        );
    }


    // =====================================================
    // GET MY CLASS ASSIGNMENTS
    // =====================================================

    @GetMapping("/my-class")
    @PreAuthorize(
            "hasAnyRole('STUDENT', 'REP', 'TEACHER')"
    )
    public ResponseEntity<List<AssignmentResponse>>
    getMyAssignments() {

        return ResponseEntity.ok(
                assignmentService.getMyAssignments()
        );
    }


    // =====================================================
    // GET ASSIGNMENT FILE
    // =====================================================


    @GetMapping("/{assignmentId}/file/{fileName:.+}")
    @PreAuthorize(
            "hasAnyRole('STUDENT', 'REP', 'TEACHER', 'ADMIN')"
    )
    public ResponseEntity<Resource> getFile(

            @PathVariable Integer assignmentId,

            @PathVariable String fileName) {

        Resource resource =
                assignmentService.getAssignmentFile(
                        assignmentId,
                        fileName
                );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\""
                                + resource.getFilename()
                                + "\""
                )
                .contentType(
                        MediaType.APPLICATION_PDF
                )
                .body(resource);
    }

    @PutMapping(
            value = "/{assignmentId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize(
            "hasAnyRole('TEACHER', 'REP', 'ADMIN')"
    )
    public ResponseEntity<AssignmentResponse> updateAssignment(

            @PathVariable Integer assignmentId,

            @RequestParam("title")
            String title,

            @RequestParam("description")
            String description,

            @RequestParam("classId")
            Integer classId,

            @RequestParam("subjectId")
            Integer subjectId,

            @RequestParam("dueDate")
            String dueDate,

            @RequestParam(value = "file", required = false)
            MultipartFile file) {

        AssignmentRequest request = new AssignmentRequest();

        request.setTitle(title);
        request.setDescription(description);
        request.setClassId(classId);
        request.setSubjectId(subjectId);
        request.setDueDate(
                LocalDateTime.parse(dueDate)
        );

        return ResponseEntity.ok(
                assignmentService.updateAssignment(
                        assignmentId,
                        request,
                        file
                )
        );
    }

}

