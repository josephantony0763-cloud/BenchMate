package com.antony.benchmate.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AssignmentResponse {

    private Integer assignmentId;

    private String title;

    private String description;

    private String fileUrl;

    private LocalDateTime dueDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // ================= CREATED BY =================

    private Integer createdBy;

    private String createdByName;

    // ================= CLASS =================

    private Integer classId;

    private String className;

    private Integer semester;

    // ================= SUBJECT =================

    private Integer subjectId;

    private String subjectName;

    private String subjectCode;
}

