package com.antony.benchmate.repository;

import com.antony.benchmate.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentRepository
        extends JpaRepository<Assignment, Integer> {

    List<Assignment> findByClassEntity_ClassId(
            Integer classId
    );

    List<Assignment> findByClassEntity_ClassIdAndSubject_SubjectId(
            Integer classId,
            Integer subjectId
    );

    List<Assignment> findByCreatedBy_UserId(
            Integer userId
    );



}

