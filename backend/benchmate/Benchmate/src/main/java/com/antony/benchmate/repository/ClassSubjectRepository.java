package com.antony.benchmate.repository;

import com.antony.benchmate.entity.ClassSubject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassSubjectRepository
        extends JpaRepository<ClassSubject, Integer> {

    List<ClassSubject> findByClassEntity_ClassId(Integer classId);

    List<ClassSubject> findBySubject_SubjectId(Integer subjectId);

    boolean existsByClassEntity_ClassIdAndSubject_SubjectId(
            Integer classId,
            Integer subjectId
    );
}


