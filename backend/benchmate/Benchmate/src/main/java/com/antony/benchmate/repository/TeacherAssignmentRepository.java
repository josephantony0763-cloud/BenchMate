package com.antony.benchmate.repository;

import com.antony.benchmate.entity.TeacherAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeacherAssignmentRepository
        extends JpaRepository<TeacherAssignment, Integer> {

    // CREATE - duplicate check
    boolean existsByTeacher_UserIdAndClassEntity_ClassIdAndSubject_SubjectId(
            Integer teacherId,
            Integer classId,
            Integer subjectId
    );

    // GET ASSIGNMENT BY TEACHER + CLASS + SUBJECT
    Optional<TeacherAssignment>
    findByTeacher_UserIdAndClassEntity_ClassIdAndSubject_SubjectId(
            Integer teacherId,
            Integer classId,
            Integer subjectId
    );

    // TEACHER CLASS ACCESS
    boolean existsByTeacher_UserIdAndClassEntity_ClassId(
            Integer teacherId,
            Integer classId
    );

    // FILTER - teacher
    List<TeacherAssignment>
    findByTeacher_UserId(Integer teacherId);

    // FILTER - class
    List<TeacherAssignment>
    findByClassEntity_ClassId(Integer classId);

    // FILTER - subject
    List<TeacherAssignment>
    findBySubject_SubjectId(Integer subjectId);

    // FILTER - teacher + class
    List<TeacherAssignment>
    findByTeacher_UserIdAndClassEntity_ClassId(
            Integer teacherId,
            Integer classId
    );

    // FILTER - teacher + subject
    List<TeacherAssignment>
    findByTeacher_UserIdAndSubject_SubjectId(
            Integer teacherId,
            Integer subjectId
    );

    // FILTER - class + subject
    List<TeacherAssignment>
    findByClassEntity_ClassIdAndSubject_SubjectId(
            Integer classId,
            Integer subjectId
    );
    List<TeacherAssignment>
    findAllByTeacher_UserIdAndClassEntity_ClassIdAndSubject_SubjectId(
            Integer teacherId,
            Integer classId,
            Integer subjectId
    );
}