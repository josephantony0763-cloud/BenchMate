package com.antony.benchmate.service;

import com.antony.benchmate.dto.request.LoginRequest;
import com.antony.benchmate.dto.request.UpdateProfileRequest;
import com.antony.benchmate.dto.response.LoginResponse;
import com.antony.benchmate.entity.User;
import com.antony.benchmate.exception.InvalidCredentialsException;
import com.antony.benchmate.repository.UserRepository;
import com.antony.benchmate.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.antony.benchmate.dto.response.UserProfileResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.antony.benchmate.dto.request.ChangePasswordRequest;
import com.antony.benchmate.exception.BadRequestException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,JwtService jwtService) {
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.jwtService=jwtService;
    }



    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->  new InvalidCredentialsException("Invalid email or password"));

        if (!user.getIsActive()) {
            throw new RuntimeException("User account is inactive");
        }

        if (!passwordEncoder.matches(request.getPassword(),user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );
        Integer classId = null;

        if (user.getClassEntity() != null) {
            classId = user.getClassEntity().getClassId();
        }

        return new LoginResponse(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                classId,
                token,
                "Login successful"
        );
    }

    public UserProfileResponse getMyProfile() {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        UserProfileResponse response =
                new UserProfileResponse();

        response.setUserId(user.getUserId());

        response.setName(user.getName());

        response.setEmail(user.getEmail());

        if (user.getRole() != null) {
            response.setRole(user.getRole().name());
        }

        response.setProfileImage(
                user.getProfileImage()
        );

        if (user.getClassEntity() != null) {

            response.setClassId(
                    user.getClassEntity().getClassId()
            );

            response.setClassName(
                    user.getClassEntity().getClassName()
            );
        }

        return response;
    }
    public UserProfileResponse updateMyProfile(
            UpdateProfileRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));


        user.setName(request.getName());

        user.setProfileImage(
                request.getProfileImage()
        );

        User updatedUser =
                userRepository.save(user);

        UserProfileResponse response =
                new UserProfileResponse();

        response.setUserId(
                updatedUser.getUserId()
        );

        response.setName(
                updatedUser.getName()
        );

        response.setEmail(
                updatedUser.getEmail()
        );

        if (updatedUser.getRole() != null) {
            response.setRole(
                    updatedUser.getRole().name()
            );
        }

        response.setProfileImage(
                updatedUser.getProfileImage()
        );

        if (updatedUser.getClassEntity() != null) {

            response.setClassId(
                    updatedUser.getClassEntity()
                            .getClassId()
            );

            response.setClassName(
                    updatedUser.getClassEntity()
                            .getClassName()
            );
        }

        return response;
    }
    public void changePassword(ChangePasswordRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword())) {

            throw new BadRequestException(
                    "Current password is incorrect"
            );
        }

        if (passwordEncoder.matches(
                request.getNewPassword(),
                user.getPassword())) {

            throw new BadRequestException(
                    "New password must be different from current password"
            );
        }

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        userRepository.save(user);
    }
}
