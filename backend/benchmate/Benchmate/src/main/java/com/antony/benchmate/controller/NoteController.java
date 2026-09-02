package com.antony.benchmate.controller;

import com.antony.benchmate.dto.response.NoteResponse;
import com.antony.benchmate.entity.ClassEntity;
import com.antony.benchmate.entity.Note;
import com.antony.benchmate.entity.Subject;
import com.antony.benchmate.service.FileStorageService;
import com.antony.benchmate.service.NoteService;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;
    private final FileStorageService fileStorageService;

    public NoteController(
            NoteService noteService,
            FileStorageService fileStorageService) {

        this.noteService = noteService;
        this.fileStorageService = fileStorageService;
    }


    // =====================================================
    // CREATE NOTE
    // =====================================================


    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<NoteResponse> createNote(
            @RequestBody Note note) {

        return ResponseEntity.ok(
                noteService.createNote(note)
        );
    }



    // =====================================================
    // UPLOAD NOTE
    // TEACHER + REP
    // =====================================================

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('TEACHER', 'REP', 'ADMIN')")
    public ResponseEntity<NoteResponse> uploadNote(

            @RequestParam("title")
            String title,

            @RequestParam("description")
            String description,

            @RequestParam("classId")
            Integer classId,

            @RequestParam("subjectId")
            Integer subjectId,

            @RequestParam("file")
            MultipartFile file) {

        Note note = new Note();

        note.setTitle(title);
        note.setDescription(description);

        ClassEntity classEntity = new ClassEntity();
        classEntity.setClassId(classId);

        Subject subject = new Subject();
        subject.setSubjectId(subjectId);

        note.setClassEntity(classEntity);
        note.setSubject(subject);

        return ResponseEntity.ok(
                noteService.createNoteWithFile(note, file)
        );
    }




    // =====================================================
    // GET ALL NOTES
    // =====================================================

    @GetMapping
    @PreAuthorize(
            "hasAnyRole('STUDENT', 'REP', 'TEACHER', 'ADMIN')"
    )
    public ResponseEntity<List<NoteResponse>> getAllNotes() {

        return ResponseEntity.ok(
                noteService.getAllNotes()
        );
    }


    // =====================================================
    // GET NOTE BY ID
    // =====================================================

    @GetMapping("/{noteId}")
    @PreAuthorize(
            "hasAnyRole('STUDENT', 'REP', 'TEACHER', 'ADMIN')"
    )
    public ResponseEntity<NoteResponse> getNoteById(
            @PathVariable Integer noteId) {

        return ResponseEntity.ok(
                noteService.getNoteById(noteId)
        );
    }


    // =====================================================
    // DELETE NOTE
    // =====================================================

    @DeleteMapping("/{noteId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<String> deleteNote(
            @PathVariable Integer noteId) {

        noteService.deleteNote(noteId);

        return ResponseEntity.ok(
                "Note deleted successfully"
        );
    }


    // =====================================================
    // GET NOTE FILE
    // =====================================================

    @GetMapping("/file/{fileName:.+}")
    @PreAuthorize(
            "hasAnyRole('STUDENT', 'REP', 'TEACHER', 'ADMIN')"
    )
    public ResponseEntity<Resource> getFile(
            @PathVariable String fileName) {

        Resource resource =
                noteService.getNoteFile(fileName);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\""
                                + resource.getFilename()
                                + "\""
                )
                .contentType(
                        MediaType.APPLICATION_PDF
                )
                .body(resource);
    }




    // =====================================================
    // GET NOTES BY CLASS
    // =====================================================

    @GetMapping("/class/{classId}")
    @PreAuthorize(
            "hasAnyRole('STUDENT', 'REP', 'TEACHER', 'ADMIN')"
    )
    public ResponseEntity<List<NoteResponse>> getNotesByClass(
            @PathVariable Integer classId) {

        return ResponseEntity.ok(
                noteService.getNotesByClass(classId)
        );
    }


    // =====================================================
    // GET NOTES BY CLASS + SUBJECT
    // =====================================================

    @GetMapping("/class/{classId}/subject/{subjectId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'REP', 'TEACHER', 'ADMIN')")
    public ResponseEntity<List<NoteResponse>>
    getNotesByClassAndSubject(

            @PathVariable Integer classId,

            @PathVariable Integer subjectId) {


        return ResponseEntity.ok(

                noteService.getNotesByClassAndSubject(
                        classId,
                        subjectId
                )
        );
    }


    // =====================================================
    // GET NOTES FOR LOGGED-IN USER'S CLASS
    // =====================================================

    @GetMapping("/my-class")
    @PreAuthorize(
            "hasAnyRole('STUDENT', 'REP', 'TEACHER')"
    )
    public ResponseEntity<List<NoteResponse>>
    getMyClassNotes() {

        return ResponseEntity.ok(
                noteService.getMyClassNotes()
        );
    }
}