package com.antony.benchmate.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NoteResponse {

    private Integer noteId;
    private String title;
    private String description;
    private String fileUrl;
    private Integer uploadedBy;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Integer classId;
    private String className;
    private Integer semester;

    private Integer subjectId;
    private String subjectName;
    private String subjectCode;

}