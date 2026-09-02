package com.antony.benchmate.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeacherAssignmentResponse {

    private Integer assignmentId;

    private Integer teacherId;
    private String teacherName;

    private Integer classId;
    private String className;

    private Integer subjectId;
    private String subjectName;
    private String subjectCode;
}