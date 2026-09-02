package com.antony.benchmate.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnnouncementRequest {

    private String title;

    private String message;

    private Integer classId;
}