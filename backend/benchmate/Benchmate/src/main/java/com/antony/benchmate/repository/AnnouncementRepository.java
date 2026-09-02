package com.antony.benchmate.repository;

import com.antony.benchmate.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnouncementRepository
        extends JpaRepository<Announcement, Integer> {

    List<Announcement> findByClassEntity_ClassId(
            Integer classId
    );

    List<Announcement> findByClassEntity_ClassIdOrderByCreatedAtDesc(
            Integer classId
    );
    List<Announcement> findAllByOrderByCreatedAtDesc();
}