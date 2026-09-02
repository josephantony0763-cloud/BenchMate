package com.antony.benchmate.service;

import com.antony.benchmate.dto.response.NoteResponse;
import com.antony.benchmate.entity.ClassEntity;
import com.antony.benchmate.entity.Note;
import com.antony.benchmate.entity.Subject;
import com.antony.benchmate.entity.User;
import com.antony.benchmate.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.antony.benchmate.exception.BadRequestException;
import com.antony.benchmate.exception.ForbiddenException;
import com.antony.benchmate.exception.ResourceNotFoundException;
import org.springframework.web.multipart.MultipartFile;


import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final ClassRepository classRepository;
    private final SubjectRepository subjectRepository;
    private final FileStorageService fileStorageService;
    private final TeacherAccessService teacherAccessService;
    private final TeacherAuthorizationService teacherAuthorizationService;
    private final TeacherAssignmentRepository teacherAssignmentRepository;




    // =====================================================
    // CREATE NOTE
    // =====================================================

    public NoteResponse createNote(Note note) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        if (note.getClassEntity() == null) {
            throw new BadRequestException("Class is required");
        }

        if (note.getSubject() == null) {
            throw new BadRequestException("Subject is required");
        }

        Integer classId = note.getClassEntity().getClassId();
        Integer subjectId = note.getSubject().getSubjectId();

        if (classId == null) {
            throw new BadRequestException("Class ID is required");
        }

        if (subjectId == null) {
            throw new BadRequestException("Subject ID is required");
        }

        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Class not found"));

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Subject not found"));

        // =====================================================
        // AUTHORIZATION
        // =====================================================

        String role = user.getRole().name();

        if ("TEACHER".equals(role)) {

            teacherAccessService.requireTeacherAccess(
                    user.getUserId(),
                    classId,
                    subjectId
            );
        }

        if ("REP".equals(role)) {

            if (user.getClassEntity() == null) {
                throw new ForbiddenException(
                        "REP is not assigned to a class"
                );
            }

            Integer repClassId =
                    user.getClassEntity().getClassId();

            if (!repClassId.equals(classId)) {
                throw new ForbiddenException(
                        "REP can only upload notes for their own class"
                );
            }
        }

        // =====================================================
        // SAVE NOTE
        // =====================================================

        note.setClassEntity(classEntity);
        note.setSubject(subject);
        note.setUploadedBy(user);

        Note savedNote = noteRepository.save(note);

        return mapToResponse(savedNote);
    }


    public NoteResponse createNoteWithFile(
            Note note,
            MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new BadRequestException("PDF file is required");
        }

        // =====================================================
        // GET LOGGED-IN USER
        // =====================================================

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));


        // =====================================================
        // VALIDATE REQUEST
        // =====================================================

        if (note.getClassEntity() == null) {
            throw new BadRequestException("Class is required");
        }

        if (note.getSubject() == null) {
            throw new BadRequestException("Subject is required");
        }

        Integer classId =
                note.getClassEntity().getClassId();

        Integer subjectId =
                note.getSubject().getSubjectId();

        if (classId == null) {
            throw new BadRequestException("Class ID is required");
        }

        if (subjectId == null) {
            throw new BadRequestException("Subject ID is required");
        }


        // =====================================================
        // LOAD CLASS + SUBJECT
        // =====================================================

        ClassEntity classEntity =
                classRepository.findById(classId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Class not found"));


        Subject subject =
                subjectRepository.findById(subjectId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Subject not found"));


        // =====================================================
        // AUTHORIZATION
        // =====================================================

        String role = user.getRole().name();

        if ("TEACHER".equals(role)) {

            teacherAccessService.requireTeacherAccess(
                    user.getUserId(),
                    classId,
                    subjectId
            );
        }


        if ("REP".equals(role)) {

            if (user.getClassEntity() == null) {
                throw new ForbiddenException(
                        "REP is not assigned to a class"
                );
            }

            Integer repClassId =
                    user.getClassEntity().getClassId();

            if (!repClassId.equals(classId)) {
                throw new ForbiddenException(
                        "REP can only upload notes for their own class"
                );
            }
        }


        // =====================================================
        // STORE FILE
        // =====================================================

        String fileName =
                fileStorageService.storeFile(file);

        note.setFileUrl(
                "/uploads/notes/" + fileName
        );


        // =====================================================
        // SAVE NOTE
        // =====================================================

        note.setClassEntity(classEntity);
        note.setSubject(subject);
        note.setUploadedBy(user);

        try {

            Note savedNote =
                    noteRepository.save(note);

            return mapToResponse(savedNote);

        } catch (RuntimeException ex) {

            // Remove file if database save fails
            fileStorageService.deleteFile(fileName);

            throw ex;
        }
    }
    public Resource getNoteFile(String fileName) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        String fileUrl = "/uploads/notes/" + fileName;

        Note note = noteRepository.findByFileUrl(fileUrl)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Note file not found"
                        ));

        String role = user.getRole().name();

        // =====================================================
        // ADMIN
        // =====================================================

        if ("ADMIN".equals(role)) {
            return fileStorageService.loadFileAsResource(
                    fileName
            );
        }

        // =====================================================
        // STUDENT
        // =====================================================

        if ("STUDENT".equals(role)) {

            if (user.getClassEntity() == null) {
                throw new ForbiddenException(
                        "Student is not assigned to a class"
                );
            }

            Integer userClassId =
                    user.getClassEntity().getClassId();

            Integer noteClassId =
                    note.getClassEntity()
                            .getClassId();

            if (!userClassId.equals(noteClassId)) {
                throw new ForbiddenException(
                        "You cannot access notes from another class"
                );
            }

            return fileStorageService.loadFileAsResource(
                    fileName
            );
        }

        // =====================================================
        // REP
        // =====================================================

        if ("REP".equals(role)) {

            if (user.getClassEntity() == null) {
                throw new ForbiddenException(
                        "REP is not assigned to a class"
                );
            }

            Integer userClassId =
                    user.getClassEntity().getClassId();

            Integer noteClassId =
                    note.getClassEntity()
                            .getClassId();

            if (!userClassId.equals(noteClassId)) {
                throw new ForbiddenException(
                        "REP can only access notes from their own class"
                );
            }

            return fileStorageService.loadFileAsResource(
                    fileName
            );
        }

        // =====================================================
        // TEACHER
        // =====================================================

        if ("TEACHER".equals(role)) {

            Integer noteClassId =
                    note.getClassEntity()
                            .getClassId();

            Integer noteSubjectId =
                    note.getSubject()
                            .getSubjectId();

            teacherAccessService.requireTeacherAccess(
                    user.getUserId(),
                    noteClassId,
                    noteSubjectId
            );

            return fileStorageService.loadFileAsResource(
                    fileName
            );
        }

        throw new ForbiddenException(
                "You are not authorized to access this file"
        );
    }



    // =====================================================
    // GET ALL NOTES
    // =====================================================

    public List<NoteResponse> getAllNotes() {
        return noteRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =====================================================
    // GET NOTE BY ID
    // =====================================================

    public NoteResponse getNoteById(Integer noteId) {
        Note note = noteRepository.findById(noteId)
                        .orElseThrow(() -> new ResourceNotFoundException("Note not found"));

        return mapToResponse(note);
    }

    // delete note
    public void deleteNote(Integer noteId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));

        if ("TEACHER".equals(user.getRole().name())) {

            if (note.getUploadedBy() == null ||
                    !note.getUploadedBy().getUserId().equals(user.getUserId())) {

                throw new ForbiddenException("You are not allowed to delete this note");
            }
        }

        String fileUrl = note.getFileUrl();

        if (fileUrl != null && !fileUrl.isBlank()) {

            String fileName = Paths.get(fileUrl).getFileName().toString();
            fileStorageService.deleteFile(fileName);
        }
        noteRepository.delete(note);
    }



    // =====================================================
    // GET NOTES BY CLASS
    // =====================================================

    public List<NoteResponse> getNotesByClass(Integer classId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        classRepository.findById(classId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Class not found"));

        String role = user.getRole().name();

        // =====================================================
        // ADMIN
        // =====================================================

        if ("ADMIN".equals(role)) {
            return noteRepository
                    .findByClassEntity_ClassId(classId)
                    .stream()
                    .map(this::mapToResponse)
                    .toList();
        }

        // =====================================================
        // STUDENT
        // =====================================================

        if ("STUDENT".equals(role)) {

            if (user.getClassEntity() == null) {
                throw new ForbiddenException(
                        "Student is not assigned to a class"
                );
            }

            Integer userClassId =
                    user.getClassEntity().getClassId();

            if (!userClassId.equals(classId)) {
                throw new ForbiddenException(
                        "You cannot access notes from another class"
                );
            }
        }

        // =====================================================
        // REP
        // =====================================================

        if ("REP".equals(role)) {

            if (user.getClassEntity() == null) {
                throw new ForbiddenException(
                        "REP is not assigned to a class"
                );
            }

            Integer repClassId =
                    user.getClassEntity().getClassId();

            if (!repClassId.equals(classId)) {
                throw new ForbiddenException(
                        "REP can only access notes from their own class"
                );
            }
        }

        // =====================================================
        // TEACHER
        // =====================================================

        if ("TEACHER".equals(role)) {

            boolean authorized =
                    teacherAuthorizationService
                            .canAccessClass(
                                    user.getUserId(),
                                    classId
                            );

            if (!authorized) {
                throw new ForbiddenException(
                        "You are not assigned to this class"
                );
            }
        }

        return noteRepository
                .findByClassEntity_ClassId(classId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET NOTES BY CLASS AND SUBJECT
    // =====================================================

    public List<NoteResponse> getNotesByClassAndSubject(
            Integer classId,
            Integer subjectId) {

        // =====================================================
        // VALIDATE CLASS
        // =====================================================

        classRepository.findById(classId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Class not found"));


        // =====================================================
        // VALIDATE SUBJECT
        // =====================================================

        subjectRepository.findById(subjectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Subject not found"));


        // =====================================================
        // GET LOGGED-IN USER
        // =====================================================

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));


        String role = user.getRole().name();


        // =====================================================
        // ADMIN
        // =====================================================

        if ("ADMIN".equals(role)) {

            return noteRepository
                    .findByClassEntity_ClassIdAndSubject_SubjectId(
                            classId,
                            subjectId
                    )
                    .stream()
                    .map(this::mapToResponse)
                    .toList();
        }


        // =====================================================
        // STUDENT
        // =====================================================

        if ("STUDENT".equals(role)) {

            if (user.getClassEntity() == null) {
                throw new ForbiddenException(
                        "Student is not assigned to a class"
                );
            }

            Integer userClassId =
                    user.getClassEntity().getClassId();

            if (!userClassId.equals(classId)) {
                throw new ForbiddenException(
                        "You cannot access notes from another class"
                );
            }

            return noteRepository
                    .findByClassEntity_ClassIdAndSubject_SubjectId(
                            classId,
                            subjectId
                    )
                    .stream()
                    .map(this::mapToResponse)
                    .toList();
        }


        // =====================================================
        // REP
        // =====================================================

        if ("REP".equals(role)) {

            if (user.getClassEntity() == null) {
                throw new ForbiddenException(
                        "REP is not assigned to a class"
                );
            }

            Integer repClassId =
                    user.getClassEntity().getClassId();

            if (!repClassId.equals(classId)) {
                throw new ForbiddenException(
                        "REP can only access notes from their own class"
                );
            }

            return noteRepository
                    .findByClassEntity_ClassIdAndSubject_SubjectId(
                            classId,
                            subjectId
                    )
                    .stream()
                    .map(this::mapToResponse)
                    .toList();
        }


        // =====================================================
        // TEACHER
        // =====================================================

        if ("TEACHER".equals(role)) {

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

            return noteRepository
                    .findByClassEntity_ClassIdAndSubject_SubjectId(
                            classId,
                            subjectId
                    )
                    .stream()
                    .map(this::mapToResponse)
                    .toList();
        }


        // =====================================================
        // OTHER ROLES
        // =====================================================

        throw new ForbiddenException(
                "You are not authorized to access these notes"
        );
    }

    // =====================================================
    // GET NOTES FOR LOGGED-IN STUDENT'S CLASS
    // =====================================================

    public List<NoteResponse> getMyClassNotes() {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        String role = user.getRole().name();

        // =====================================================
        // STUDENT / REP
        // =====================================================

        if ("STUDENT".equals(role) || "REP".equals(role)) {

            if (user.getClassEntity() == null) {
                throw new ForbiddenException(
                        role + " is not assigned to a class"
                );
            }

            Integer classId =
                    user.getClassEntity().getClassId();

            return noteRepository
                    .findByClassEntity_ClassId(classId)
                    .stream()
                    .map(this::mapToResponse)
                    .toList();
        }

        // =====================================================
        // TEACHER
        // =====================================================

        if ("TEACHER".equals(role)) {

            return teacherAssignmentRepository
                    .findByTeacher_UserId(user.getUserId())
                    .stream()
                    .flatMap(assignment ->
                            noteRepository
                                    .findByClassEntity_ClassIdAndSubject_SubjectId(
                                            assignment.getClassEntity().getClassId(),
                                            assignment.getSubject().getSubjectId()
                                    )
                                    .stream()
                    )
                    .map(this::mapToResponse)
                    .toList();
        }

        // =====================================================
        // ADMIN
        // =====================================================

        if ("ADMIN".equals(role)) {

            return noteRepository.findAll()
                    .stream()
                    .map(this::mapToResponse)
                    .toList();
        }

        throw new ForbiddenException(
                "You are not authorized to access notes"
        );
    }

    // =====================================================
    // MAP ENTITY → RESPONSE DTO
    // =====================================================

    private NoteResponse mapToResponse(Note note) {
        NoteResponse response = new NoteResponse();
        response.setNoteId(note.getNoteId());
        response.setTitle(note.getTitle());
        response.setDescription(note.getDescription());

        if (note.getFileUrl() != null && !note.getFileUrl().isBlank()) {
            String fileName = Paths.get(note.getFileUrl()).getFileName().toString();
            response.setFileUrl("/api/notes/file/" + fileName);
        }
        else {response.setFileUrl(null);}

        response.setUploadedBy(note.getUploadedBy().getUserId());
        response.setCreatedAt(note.getCreatedAt());
        response.setUpdatedAt(note.getUpdatedAt());

        // ================= CLASS =================

        if (note.getClassEntity() != null) {
            response.setClassId(note.getClassEntity().getClassId());
            response.setClassName(note.getClassEntity().getClassName());
            response.setSemester(note.getClassEntity().getSemester());
        }

        // ================= SUBJECT =================

        if (note.getSubject() != null) {
            response.setSubjectId(note.getSubject().getSubjectId());
            response.setSubjectName(note.getSubject().getSubjectName());
            response.setSubjectCode(note.getSubject().getSubjectCode());
        }

        return response;
    }
}