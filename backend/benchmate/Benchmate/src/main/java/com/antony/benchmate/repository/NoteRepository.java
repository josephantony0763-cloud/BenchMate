package com.antony.benchmate.repository;

import com.antony.benchmate.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note,Integer> {
    List<Note> findByClassEntity_ClassId(Integer classId);
    Optional<Note> findByFileUrl(String fileUrl);

    List<Note> findByClassEntity_ClassIdAndSubject_SubjectId(Integer classId, Integer subjectId);
}
