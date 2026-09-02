package com.antony.benchmate.service;

import com.antony.benchmate.dto.response.AssignmentResponse;
import com.antony.benchmate.entity.Assignment;
import com.antony.benchmate.entity.ClassEntity;
import com.antony.benchmate.entity.Subject;
import com.antony.benchmate.entity.User;
import com.antony.benchmate.exception.BadRequestException;
import com.antony.benchmate.exception.ForbiddenException;
import com.antony.benchmate.exception.ResourceNotFoundException;
import com.antony.benchmate.repository.AssignmentRepository;
import com.antony.benchmate.repository.ClassRepository;
import com.antony.benchmate.repository.SubjectRepository;
import com.antony.benchmate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;
import com.antony.benchmate.dto.request.AssignmentRequest;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Paths;


import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;
    private final ClassRepository classRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherAuthorizationService teacherAuthorizationService;
    private final FileStorageService fileStorageService;


    // =====================================================
    // CREATE ASSIGNMENT
    // =====================================================

    public AssignmentResponse createAssignment(
            Assignment assignment) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));


        // =================================================
        // VALIDATE CLASS
        // =================================================

        if (assignment.getClassEntity() == null) {
            throw new BadRequestException(
                    "Class is required"
            );
        }

        Integer classId =
                assignment.getClassEntity().getClassId();

        if (classId == null) {
            throw new BadRequestException(
                    "Class ID is required"
            );
        }

        ClassEntity classEntity =
                classRepository.findById(classId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Class not found"
                                ));


        // =================================================
        // VALIDATE SUBJECT
        // =================================================

        if (assignment.getSubject() == null) {
            throw new BadRequestException(
                    "Subject is required"
            );
        }

        Integer subjectId =
                assignment.getSubject().getSubjectId();

        if (subjectId == null) {
            throw new BadRequestException(
                    "Subject ID is required"
            );

        }

        Subject subject =
                subjectRepository.findById(subjectId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Subject not found"
                                ));


        // =================================================
        // VALIDATE TITLE
        // =================================================

        if (assignment.getTitle() == null ||
                assignment.getTitle().isBlank()) {

            throw new BadRequestException(
                    "Assignment title is required"
            );
        }


        // =================================================
        // VALIDATE DUE DATE
        // =================================================

        if (assignment.getDueDate() == null) {

            throw new BadRequestException(
                    "Due date is required"
            );
        }


        // =================================================
        // TEACHER AUTHORIZATION
        // =================================================

        if ("TEACHER".equals(user.getRole().name())) {

            boolean authorized =
                    teacherAuthorizationService.canAccess(
                            user.getUserId(),
                            classId,
                            subjectId
                    );

            if (!authorized) {

                throw new ForbiddenException(
                        "You are not assigned to this class and subject"
                );
            }
        }


        // =================================================
        // REP AUTHORIZATION
        // =================================================

        if ("REP".equals(user.getRole().name())) {

            if (user.getClassEntity() == null) {

                throw new ForbiddenException(
                        "REP is not assigned to a class"
                );
            }

            Integer repClassId =
                    user.getClassEntity().getClassId();

            if (!repClassId.equals(classId)) {

                throw new ForbiddenException(
                        "REP can only create assignments for their own class"
                );
            }
        }


        // =================================================
        // SET RELATIONSHIPS
        // =================================================

        assignment.setClassEntity(classEntity);

        assignment.setSubject(subject);

        assignment.setCreatedBy(user);


        // =================================================
        // SAVE
        // =================================================

        Assignment savedAssignment =
                assignmentRepository.save(assignment);


        return mapToResponse(savedAssignment);
    }


    // =====================================================
    // GET ALL ASSIGNMENTS
    // =====================================================

    public List<AssignmentResponse> getAllAssignments() {

        return assignmentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =====================================================
    // GET ASSIGNMENT BY ID
    // =====================================================

    public AssignmentResponse getAssignmentById(
            Integer assignmentId) {

        Assignment assignment =
                assignmentRepository.findById(assignmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Assignment not found"
                                ));

        return mapToResponse(assignment);
    }


    // =====================================================
    // GET ASSIGNMENTS BY CLASS
    // =====================================================

    public List<AssignmentResponse> getAssignmentsByClass(
            Integer classId) {

        classRepository.findById(classId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Class not found"
                        ));

        return assignmentRepository
                .findByClassEntity_ClassId(classId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =====================================================
    // GET ASSIGNMENTS BY CLASS + SUBJECT
    // =====================================================

    public List<AssignmentResponse>
    getAssignmentsByClassAndSubject(
            Integer classId,
            Integer subjectId) {

        classRepository.findById(classId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Class not found"
                        ));

        subjectRepository.findById(subjectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Subject not found"
                        ));

        return assignmentRepository
                .findByClassEntity_ClassIdAndSubject_SubjectId(
                        classId,
                        subjectId
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =====================================================
    // GET MY ASSIGNMENTS
    // =====================================================

    public List<AssignmentResponse> getMyAssignments() {

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
                    "User is not assigned to a class"
            );
        }

        Integer classId =
                user.getClassEntity().getClassId();

        return assignmentRepository
                .findByClassEntity_ClassId(classId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =====================================================
    // MAP ENTITY → RESPONSE
    // =====================================================

    private AssignmentResponse mapToResponse(
            Assignment assignment) {

        AssignmentResponse response =
                new AssignmentResponse();

        response.setAssignmentId(
                assignment.getAssignmentId()
        );

        response.setTitle(
                assignment.getTitle()
        );

        response.setDescription(
                assignment.getDescription()
        );

        if (assignment.getFileUrl() != null
                && !assignment.getFileUrl().isBlank()) {

            String fileName =
                    Paths.get(assignment.getFileUrl())
                            .getFileName()
                            .toString();

            response.setFileUrl(
                    "/api/assignments/"
                            + assignment.getAssignmentId()
                            + "/file/"
                            + fileName
            );

        } else {

            response.setFileUrl(null);
        }



        response.setDueDate(
                assignment.getDueDate()
        );

        response.setCreatedAt(
                assignment.getCreatedAt()
        );

        response.setUpdatedAt(
                assignment.getUpdatedAt()
        );


        // =================================================
        // CREATED BY
        // =================================================

        if (assignment.getCreatedBy() != null) {

            response.setCreatedBy(
                    assignment.getCreatedBy().getUserId()
            );

            response.setCreatedByName(
                    assignment.getCreatedBy().getName()
            );
        }


        // =================================================
        // CLASS
        // =================================================

        if (assignment.getClassEntity() != null) {

            response.setClassId(
                    assignment.getClassEntity().getClassId()
            );

            response.setClassName(
                    assignment.getClassEntity().getClassName()
            );

            response.setSemester(
                    assignment.getClassEntity().getSemester()
            );
        }


        // =================================================
        // SUBJECT
        // =================================================

        if (assignment.getSubject() != null) {

            response.setSubjectId(
                    assignment.getSubject().getSubjectId()
            );

            response.setSubjectName(
                    assignment.getSubject().getSubjectName()
            );

            response.setSubjectCode(
                    assignment.getSubject().getSubjectCode()
            );
        }

        return response;
    }

    public void validateAssignmentAccess(Assignment assignment) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        if (assignment.getClassEntity() == null) {
            throw new BadRequestException(
                    "Class is required"
            );
        }

        if (assignment.getSubject() == null) {
            throw new BadRequestException(
                    "Subject is required"
            );
        }

        Integer classId =
                assignment.getClassEntity().getClassId();

        Integer subjectId =
                assignment.getSubject().getSubjectId();

        if (classId == null) {
            throw new BadRequestException(
                    "Class ID is required"
            );
        }

        if (subjectId == null) {
            throw new BadRequestException(
                    "Subject ID is required"
            );
        }

        classRepository.findById(classId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Class not found"
                        ));

        subjectRepository.findById(subjectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Subject not found"
                        ));


        // =========================
        // TEACHER AUTHORIZATION
        // =========================

        if ("TEACHER".equals(user.getRole().name())) {

            boolean authorized =
                    teacherAuthorizationService.canAccess(
                            user.getUserId(),
                            classId,
                            subjectId
                    );

            if (!authorized) {

                throw new ForbiddenException(
                        "You are not assigned to this class and subject"
                );
            }
        }


        // =========================
        // REP AUTHORIZATION
        // =========================

        if ("REP".equals(user.getRole().name())) {

            if (user.getClassEntity() == null ||
                    !user.getClassEntity()
                            .getClassId()
                            .equals(classId)) {

                throw new ForbiddenException(
                        "REP can only create assignments for their own class"
                );
            }
        }
    }
    public Resource getAssignmentFile(
            Integer assignmentId,
            String fileName) {

        Assignment assignment =
                assignmentRepository.findById(assignmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Assignment not found"
                                ));

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                ));

        Integer assignmentClassId =
                assignment.getClassEntity()
                        .getClassId();


        // =========================
        // ADMIN
        // =========================

        if ("ADMIN".equals(user.getRole().name())) {
            return loadAssignmentFile(
                    assignment,
                    fileName
            );
        }


        // =========================
        // STUDENT / REP
        // =========================

        if ("STUDENT".equals(user.getRole().name())
                || "REP".equals(user.getRole().name())) {

            if (user.getClassEntity() == null
                    || !user.getClassEntity()
                    .getClassId()
                    .equals(assignmentClassId)) {

                throw new ForbiddenException(
                        "You are not allowed to access this assignment"
                );
            }

            return loadAssignmentFile(
                    assignment,
                    fileName
            );
        }


        // =========================
        // TEACHER
        // =========================

        if ("TEACHER".equals(user.getRole().name())) {

            boolean authorized =
                    teacherAuthorizationService.canAccess(
                            user.getUserId(),
                            assignmentClassId,
                            assignment.getSubject()
                                    .getSubjectId()
                    );

            if (!authorized) {

                throw new ForbiddenException(
                        "You are not assigned to this class and subject"
                );
            }

            return loadAssignmentFile(
                    assignment,
                    fileName
            );
        }


        throw new ForbiddenException(
                "You are not allowed to access this assignment"
        );
    }

    private Resource loadAssignmentFile(
            Assignment assignment,
            String fileName) {

        if (assignment.getFileUrl() == null
                || assignment.getFileUrl().isBlank()) {

            throw new ResourceNotFoundException(
                    "Assignment file not found"
            );
        }

        String storedFileName =
                java.nio.file.Paths
                        .get(assignment.getFileUrl())
                        .getFileName()
                        .toString();


        if (!storedFileName.equals(fileName)) {

            throw new ResourceNotFoundException(
                    "Assignment file not found"
            );
        }

        return fileStorageService
                .loadFileAsResource(fileName);
    }

    public void deleteAssignment(Integer assignmentId) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        Assignment assignment =
                assignmentRepository.findById(assignmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Assignment not found"
                                ));


        Integer classId =
                assignment.getClassEntity().getClassId();

        Integer subjectId =
                assignment.getSubject().getSubjectId();


        // =========================
        // ADMIN
        // =========================

        if ("ADMIN".equals(user.getRole().name())) {

            deleteAssignmentFile(assignment);

            assignmentRepository.delete(assignment);

            return;
        }


        // =========================
        // TEACHER
        // =========================

        if ("TEACHER".equals(user.getRole().name())) {

            boolean authorized =
                    teacherAuthorizationService.canAccess(
                            user.getUserId(),
                            classId,
                            subjectId
                    );

            if (!authorized) {

                throw new ForbiddenException(
                        "You are not assigned to this class and subject"
                );
            }

            deleteAssignmentFile(assignment);

            assignmentRepository.delete(assignment);

            return;
        }


        throw new ForbiddenException(
                "You are not allowed to delete this assignment"
        );
    }


    private void deleteAssignmentFile(
            Assignment assignment) {

        String fileUrl = assignment.getFileUrl();

        if (fileUrl == null || fileUrl.isBlank()) {
            return;
        }

        String fileName =
                java.nio.file.Paths
                        .get(fileUrl)
                        .getFileName()
                        .toString();

        fileStorageService.deleteFile(fileName);
    }
    public List<AssignmentResponse> getMyClassAssignments() {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                ));

        if (user.getClassEntity() == null) {
            throw new BadRequestException(
                    "User is not assigned to a class"
            );
        }

        Integer classId =
                user.getClassEntity().getClassId();

        return assignmentRepository
                .findByClassEntity_ClassId(classId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public AssignmentResponse updateAssignment(
            Integer assignmentId,
            AssignmentRequest request,
            MultipartFile file) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        Assignment assignment =
                assignmentRepository.findById(assignmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Assignment not found"
                                ));

        if (request.getTitle() == null ||
                request.getTitle().isBlank()) {

            throw new BadRequestException(
                    "Title is required"
            );
        }

        if (request.getDueDate() == null) {

            throw new BadRequestException(
                    "Due date is required"
            );
        }

        if (request.getClassId() == null) {

            throw new BadRequestException(
                    "Class ID is required"
            );
        }

        if (request.getSubjectId() == null) {

            throw new BadRequestException(
                    "Subject ID is required"
            );
        }

        Integer classId = request.getClassId();
        Integer subjectId = request.getSubjectId();

        ClassEntity classEntity =
                classRepository.findById(classId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Class not found"
                                ));

        Subject subject =
                subjectRepository.findById(subjectId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Subject not found"
                                ));

        // ================= AUTHORIZATION =================

        if ("TEACHER".equals(user.getRole().name())) {

            boolean authorized =
                    teacherAuthorizationService.canAccess(
                            user.getUserId(),
                            classId,
                            subjectId
                    );

            if (!authorized) {

                throw new ForbiddenException(
                        "You are not assigned to this class and subject"
                );
            }
        }

        if ("REP".equals(user.getRole().name())) {

            if (user.getClassEntity() == null ||
                    !user.getClassEntity()
                            .getClassId()
                            .equals(classId)) {

                throw new ForbiddenException(
                        "REP can only update assignments for their own class"
                );
            }
        }

        // ================= UPDATE DATA =================

        assignment.setTitle(request.getTitle());
        assignment.setDescription(request.getDescription());
        assignment.setDueDate(request.getDueDate());
        assignment.setClassEntity(classEntity);
        assignment.setSubject(subject);

        // ================= REPLACE FILE =================

        if (file != null && !file.isEmpty()) {

            String oldFileUrl =
                    assignment.getFileUrl();

            String newFileName =
                    fileStorageService.storeFile(file);

            assignment.setFileUrl(
                    "/uploads/notes/" + newFileName
            );

            if (oldFileUrl != null &&
                    !oldFileUrl.isBlank()) {

                String oldFileName =
                        Paths.get(oldFileUrl)
                                .getFileName()
                                .toString();

                fileStorageService.deleteFile(
                        oldFileName
                );
            }
        }

        Assignment savedAssignment =
                assignmentRepository.save(assignment);

        return mapToResponse(savedAssignment);
    }
}

