package com.antony.benchmate.repository;

import com.antony.benchmate.entity.TeacherSubject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeacherSubjectRepository
        extends JpaRepository<TeacherSubject, Integer> {

    List<TeacherSubject> findByTeacher_UserId(Integer teacherId);

    List<TeacherSubject> findByClassEntity_ClassId(Integer classId);

    List<TeacherSubject> findBySubject_SubjectId(Integer subjectId);

    Optional<TeacherSubject> findByTeacher_UserIdAndClassEntity_ClassIdAndSubject_SubjectId(
            Integer teacherId,
            Integer classId,
            Integer subjectId
    );

    boolean existsByTeacher_UserIdAndClassEntity_ClassIdAndSubject_SubjectId(
            Integer teacherId,
            Integer classId,
            Integer subjectId
    );
}

