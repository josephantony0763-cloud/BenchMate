package com.antony.benchmate.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileResponse {

    private Integer userId;

    private String name;

    private String email;

    private String role;

    private String profileImage;

    private Integer classId;

    private String className;
}