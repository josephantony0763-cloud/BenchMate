package com.antony.benchmate.controller;

import com.antony.benchmate.dto.request.ChangePasswordRequest;
import com.antony.benchmate.dto.request.LoginRequest;
import com.antony.benchmate.dto.request.UpdateProfileRequest;
import com.antony.benchmate.dto.response.LoginResponse;
import com.antony.benchmate.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.antony.benchmate.dto.response.UserProfileResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        return ResponseEntity.ok(authService.login(request));
    }
    @GetMapping("/me")
    @PreAuthorize(
            "hasAnyRole('STUDENT', 'TEACHER', 'REP', 'ADMIN')"
    )
    public ResponseEntity<UserProfileResponse> getMyProfile() {

        return ResponseEntity.ok(
                authService.getMyProfile()
        );
    }
    @PutMapping("/me")
    @PreAuthorize(
            "hasAnyRole('STUDENT', 'TEACHER', 'REP', 'ADMIN')"
    )
    public ResponseEntity<UserProfileResponse> updateMyProfile(
           @Valid @RequestBody UpdateProfileRequest request) {

        return ResponseEntity.ok(
                authService.updateMyProfile(request)
        );
    }
    @PutMapping("/change-password")
    @PreAuthorize(
            "hasAnyRole('STUDENT', 'TEACHER', 'REP', 'ADMIN')"
    )
    public ResponseEntity<Map<String, String>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request) {

        authService.changePassword(request);

        return ResponseEntity.ok(
                Map.of("message", "Password changed successfully")
        );
    }


}
