package com.antony.benchmate.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AssignmentRequest {

    private String title;

    private String description;

    private Integer classId;

    private Integer subjectId;

    private LocalDateTime dueDate;
}