package com.antony.benchmate.controller;

import com.antony.benchmate.dto.request.AnnouncementRequest;
import com.antony.benchmate.dto.response.AnnouncementResponse;
import com.antony.benchmate.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    // =====================================================
    // CREATE ANNOUNCEMENT
    // =====================================================

    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'REP', 'ADMIN')")
    public ResponseEntity<AnnouncementResponse> createAnnouncement(
            @RequestBody AnnouncementRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        announcementService.createAnnouncement(request)
                );
    }
    @GetMapping("/{announcementId}")
    @PreAuthorize(
            "hasAnyRole('STUDENT', 'REP', 'TEACHER', 'ADMIN')"
    )
    public ResponseEntity<AnnouncementResponse> getAnnouncementById(
            @PathVariable Integer announcementId) {

        return ResponseEntity.ok(
                announcementService.getAnnouncementById(
                        announcementId
                )
        );
    }
    @GetMapping
    @PreAuthorize(
            "hasAnyRole('STUDENT', 'REP', 'TEACHER', 'ADMIN')"
    )
    public ResponseEntity<List<AnnouncementResponse>>
    getAllAnnouncements() {

        return ResponseEntity.ok(
                announcementService.getAllAnnouncements()
        );
    }
    @GetMapping("/class/{classId}")
    @PreAuthorize(
            "hasAnyRole('STUDENT', 'REP', 'TEACHER', 'ADMIN')"
    )
    public ResponseEntity<List<AnnouncementResponse>>
    getAnnouncementsByClass(
            @PathVariable Integer classId) {

        return ResponseEntity.ok(
                announcementService.getAnnouncementsByClass(
                        classId
                )
        );
    }
    @GetMapping("/my-class")
    @PreAuthorize(
            "hasAnyRole('STUDENT', 'REP', 'TEACHER')"
    )
    public ResponseEntity<List<AnnouncementResponse>>
    getMyClassAnnouncements() {

        return ResponseEntity.ok(
                announcementService
                        .getMyClassAnnouncements()
        );
    }
    @PutMapping("/{announcementId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'REP', 'ADMIN')")
    public ResponseEntity<AnnouncementResponse> updateAnnouncement(
            @PathVariable Integer announcementId,
            @RequestBody AnnouncementRequest request) {

        return ResponseEntity.ok(
                announcementService.updateAnnouncement(
                        announcementId,
                        request
                )
        );
    }
    @DeleteMapping("/{announcementId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'REP', 'ADMIN')")
    public ResponseEntity<String> deleteAnnouncement(
            @PathVariable Integer announcementId) {

        announcementService.deleteAnnouncement(
                announcementId
        );

        return ResponseEntity.ok(
                "Announcement deleted successfully"
        );
    }
}