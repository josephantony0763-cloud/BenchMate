package com.antony.benchmate.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTeacherAssignmentRequest {

    @NotNull(message = "Teacher ID is required")
    private Integer teacherId;

    @NotNull(message = "Class ID is required")
    private Integer classId;

    @NotNull(message = "Subject ID is required")
    private Integer subjectId;
}