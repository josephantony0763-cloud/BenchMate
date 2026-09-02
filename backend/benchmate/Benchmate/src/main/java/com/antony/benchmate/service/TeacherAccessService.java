package com.antony.benchmate.service;

import com.antony.benchmate.exception.ForbiddenException;
import com.antony.benchmate.repository.TeacherAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherAccessService {

    private final TeacherAssignmentRepository teacherAssignmentRepository;

    public boolean isTeacherAssigned(
            Integer teacherId,
            Integer classId,
            Integer subjectId) {

        return teacherAssignmentRepository
                .findByTeacher_UserIdAndClassEntity_ClassIdAndSubject_SubjectId(
                        teacherId,
                        classId,
                        subjectId
                )
                .isPresent();
    }

    public void requireTeacherAccess(
            Integer teacherId,
            Integer classId,
            Integer subjectId) {

        if (!isTeacherAssigned(teacherId, classId, subjectId)) {
            throw new ForbiddenException(
                    "You are not assigned to this class and subject"
            );
        }
    }
}