package com.antony.benchmate.service;

import com.antony.benchmate.dto.request.AnnouncementRequest;
import com.antony.benchmate.dto.response.AnnouncementResponse;
import com.antony.benchmate.entity.Announcement;
import com.antony.benchmate.entity.ClassEntity;
import com.antony.benchmate.entity.User;
import com.antony.benchmate.exception.BadRequestException;
import com.antony.benchmate.exception.ForbiddenException;
import com.antony.benchmate.exception.ResourceNotFoundException;
import com.antony.benchmate.repository.AnnouncementRepository;
import com.antony.benchmate.repository.ClassRepository;
import com.antony.benchmate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;
    private final ClassRepository classRepository;

    // =====================================================
    // CREATE ANNOUNCEMENT
    // =====================================================

    public AnnouncementResponse createAnnouncement(
            AnnouncementRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        // ================= VALIDATION =================

        if (request.getTitle() == null ||
                request.getTitle().isBlank()) {

            throw new BadRequestException(
                    "Title is required"
            );
        }

        if (request.getMessage() == null ||
                request.getMessage().isBlank()) {

            throw new BadRequestException(
                    "Message is required"
            );
        }

        if (request.getClassId() == null) {

            throw new BadRequestException(
                    "Class ID is required"
            );
        }

        // ================= CLASS =================

        ClassEntity classEntity =
                classRepository.findById(
                        request.getClassId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Class not found"
                        ));

        // ================= AUTHORIZATION =================

        String role = user.getRole().name();

        if ("STUDENT".equals(role)) {

            throw new ForbiddenException(
                    "Students cannot create announcements"
            );
        }

        if ("TEACHER".equals(role)) {

            if (user.getClassEntity() == null ||
                    !user.getClassEntity()
                            .getClassId()
                            .equals(request.getClassId())) {

                throw new ForbiddenException(
                        "You are not assigned to this class"
                );
            }
        }

        if ("REP".equals(role)) {

            if (user.getClassEntity() == null ||
                    !user.getClassEntity()
                            .getClassId()
                            .equals(request.getClassId())) {

                throw new ForbiddenException(
                        "REP can only create announcements for their own class"
                );
            }
        }

        // ================= CREATE =================

        Announcement announcement =
                new Announcement();

        announcement.setTitle(
                request.getTitle()
        );

        announcement.setMessage(
                request.getMessage()
        );

        announcement.setClassEntity(
                classEntity
        );

        announcement.setCreatedBy(
                user
        );

        Announcement savedAnnouncement =
                announcementRepository.save(
                        announcement
                );

        return mapToResponse(savedAnnouncement);
    }

    // =====================================================
    // MAP ENTITY → RESPONSE
    // =====================================================

    private AnnouncementResponse mapToResponse(
            Announcement announcement) {

        AnnouncementResponse response =
                new AnnouncementResponse();

        response.setAnnouncementId(
                announcement.getAnnouncementId()
        );

        response.setTitle(
                announcement.getTitle()
        );

        response.setMessage(
                announcement.getMessage()
        );

        response.setCreatedAt(
                announcement.getCreatedAt()
        );

        response.setUpdatedAt(
                announcement.getUpdatedAt()
        );

        if (announcement.getClassEntity() != null) {

            response.setClassId(
                    announcement.getClassEntity()
                            .getClassId()
            );

            response.setClassName(
                    announcement.getClassEntity()
                            .getClassName()
            );
        }

        if (announcement.getCreatedBy() != null) {

            response.setCreatedBy(
                    announcement.getCreatedBy()
                            .getUserId()
            );

            response.setCreatedByName(
                    announcement.getCreatedBy()
                            .getName()
            );
        }

        return response;
    }
    public AnnouncementResponse getAnnouncementById(
            Integer announcementId) {

        Announcement announcement =
                announcementRepository.findById(announcementId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Announcement not found"
                                ));

        return mapToResponse(announcement);
    }
    public List<AnnouncementResponse> getAllAnnouncements() {

        return announcementRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    public List<AnnouncementResponse> getAnnouncementsByClass(
            Integer classId) {

        classRepository.findById(classId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Class not found"
                        ));

        return announcementRepository
                .findByClassEntity_ClassIdOrderByCreatedAtDesc(classId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    public List<AnnouncementResponse> getMyClassAnnouncements() {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        if (user.getClassEntity() == null) {

            throw new ForbiddenException(
                    "You are not assigned to a class"
            );
        }

        Integer classId =
                user.getClassEntity().getClassId();

        return announcementRepository
                .findByClassEntity_ClassIdOrderByCreatedAtDesc(
                        classId
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    public AnnouncementResponse updateAnnouncement(
            Integer announcementId,
            AnnouncementRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        Announcement announcement =
                announcementRepository.findById(announcementId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Announcement not found"
                                ));

        if (request.getTitle() == null ||
                request.getTitle().isBlank()) {

            throw new BadRequestException(
                    "Title is required"
            );
        }

        if (request.getMessage() == null ||
                request.getMessage().isBlank()) {

            throw new BadRequestException(
                    "Message is required"
            );
        }

        if (request.getClassId() == null) {

            throw new BadRequestException(
                    "Class ID is required"
            );
        }

        ClassEntity classEntity =
                classRepository.findById(
                        request.getClassId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Class not found"
                        ));

        String role = user.getRole().name();

        if ("STUDENT".equals(role)) {

            throw new ForbiddenException(
                    "Students cannot update announcements"
            );
        }

        Integer announcementClassId =
                announcement.getClassEntity()
                        .getClassId();

        if ("TEACHER".equals(role) ||
                "REP".equals(role)) {

            if (user.getClassEntity() == null ||
                    !user.getClassEntity()
                            .getClassId()
                            .equals(announcementClassId)) {

                throw new ForbiddenException(
                        "You are not allowed to update this announcement"
                );
            }

            if (!announcementClassId.equals(
                    request.getClassId())) {

                throw new ForbiddenException(
                        "You cannot move an announcement to another class"
                );
            }
        }

        announcement.setTitle(
                request.getTitle()
        );

        announcement.setMessage(
                request.getMessage()
        );

        announcement.setClassEntity(
                classEntity
        );

        Announcement updatedAnnouncement =
                announcementRepository.save(
                        announcement
                );

        return mapToResponse(updatedAnnouncement);
    }
    public void deleteAnnouncement(Integer announcementId) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        Announcement announcement =
                announcementRepository.findById(announcementId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Announcement not found"
                                ));

        String role = user.getRole().name();

        if ("STUDENT".equals(role)) {

            throw new ForbiddenException(
                    "Students cannot delete announcements"
            );
        }

        Integer announcementClassId =
                announcement.getClassEntity()
                        .getClassId();

        if ("TEACHER".equals(role) ||
                "REP".equals(role)) {

            if (user.getClassEntity() == null ||
                    !user.getClassEntity()
                            .getClassId()
                            .equals(announcementClassId)) {

                throw new ForbiddenException(
                        "You are not allowed to delete this announcement"
                );
            }
        }

        announcementRepository.delete(announcement);
    }
}