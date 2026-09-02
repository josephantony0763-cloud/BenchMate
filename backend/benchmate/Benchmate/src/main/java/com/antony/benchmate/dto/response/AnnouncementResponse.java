package com.antony.benchmate.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AnnouncementResponse {

    private Integer announcementId;

    private String title;

    private String message;

    private Integer classId;

    private String className;

    private Integer createdBy;

    private String createdByName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}