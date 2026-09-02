package com.antony.benchmate.service;

import com.antony.benchmate.repository.TeacherAssignmentRepository;
import com.antony.benchmate.repository.TeacherSubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherAuthorizationService {

    private final TeacherSubjectRepository teacherSubjectRepository;
    private final TeacherAssignmentRepository teacherAssignmentRepository;

    public boolean canAccess(
            Integer teacherId,
            Integer classId,
            Integer subjectId
    ) {
        return teacherSubjectRepository
                .existsByTeacher_UserIdAndClassEntity_ClassIdAndSubject_SubjectId(
                        teacherId,
                        classId,
                        subjectId
                );
    }
    public boolean canAccessClass(
            Integer teacherId,
            Integer classId) {

        return teacherAssignmentRepository
                .existsByTeacher_UserIdAndClassEntity_ClassId(
                        teacherId,
                        classId
                );
    }
}

