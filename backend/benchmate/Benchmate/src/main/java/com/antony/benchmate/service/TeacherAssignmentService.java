package com.antony.benchmate.service;

import com.antony.benchmate.dto.request.CreateTeacherAssignmentRequest;
import com.antony.benchmate.dto.response.TeacherAssignmentResponse;
import com.antony.benchmate.entity.ClassEntity;
import com.antony.benchmate.entity.Role;
import com.antony.benchmate.entity.Subject;
import com.antony.benchmate.entity.TeacherAssignment;
import com.antony.benchmate.entity.User;
import com.antony.benchmate.exception.BadRequestException;
import com.antony.benchmate.exception.ResourceNotFoundException;
import com.antony.benchmate.repository.ClassRepository;
import com.antony.benchmate.repository.SubjectRepository;
import com.antony.benchmate.repository.TeacherAssignmentRepository;
import com.antony.benchmate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherAssignmentService {

    private final TeacherAssignmentRepository teacherAssignmentRepository;
    private final UserRepository userRepository;
    private final ClassRepository classRepository;
    private final SubjectRepository subjectRepository;

    public TeacherAssignmentResponse create(
            CreateTeacherAssignmentRequest request) {

        User teacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Teacher not found"));

        if (teacher.getRole() != Role.TEACHER) {
            throw new BadRequestException(
                    "Selected user is not a teacher"
            );
        }

        ClassEntity classEntity =
                classRepository.findById(request.getClassId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Class not found"
                                ));

        Subject subject =
                subjectRepository.findById(request.getSubjectId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Subject not found"
                                ));

        boolean exists =
                teacherAssignmentRepository
                        .existsByTeacher_UserIdAndClassEntity_ClassIdAndSubject_SubjectId(
                                teacher.getUserId(),
                                classEntity.getClassId(),
                                subject.getSubjectId()
                        );

        if (exists) {
            throw new BadRequestException(
                    "Teacher is already assigned to this class and subject"
            );
        }

        TeacherAssignment assignment =
                new TeacherAssignment();

        assignment.setTeacher(teacher);
        assignment.setClassEntity(classEntity);
        assignment.setSubject(subject);

        TeacherAssignment saved =
                teacherAssignmentRepository.save(assignment);

        return new TeacherAssignmentResponse(
                saved.getAssignmentId(),
                teacher.getUserId(),
                teacher.getName(),
                classEntity.getClassId(),
                classEntity.getClassName(),
                subject.getSubjectId(),
                subject.getSubjectName(),
                subject.getSubjectCode()
        );
    }
    public List<TeacherAssignmentResponse> getAll() {

        return teacherAssignmentRepository.findAll()
                .stream()
                .map(assignment -> new TeacherAssignmentResponse(
                        assignment.getAssignmentId(),

                        assignment.getTeacher().getUserId(),
                        assignment.getTeacher().getName(),

                        assignment.getClassEntity().getClassId(),
                        assignment.getClassEntity().getClassName(),

                        assignment.getSubject().getSubjectId(),
                        assignment.getSubject().getSubjectName(),
                        assignment.getSubject().getSubjectCode()
                ))
                .toList();
    }
    public TeacherAssignmentResponse getById(Integer assignmentId) {

        TeacherAssignment assignment =
                teacherAssignmentRepository.findById(assignmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Teacher assignment not found"
                                ));

        return new TeacherAssignmentResponse(
                assignment.getAssignmentId(),

                assignment.getTeacher().getUserId(),
                assignment.getTeacher().getName(),

                assignment.getClassEntity().getClassId(),
                assignment.getClassEntity().getClassName(),

                assignment.getSubject().getSubjectId(),
                assignment.getSubject().getSubjectName(),
                assignment.getSubject().getSubjectCode()
        );

    }
    public void delete(Integer assignmentId) {

        TeacherAssignment assignment =
                teacherAssignmentRepository.findById(assignmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Teacher assignment not found"
                                ));

        teacherAssignmentRepository.delete(assignment);
    }
    public List<TeacherAssignmentResponse> getMyAssignments() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User teacher = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Teacher not found"));

        return teacherAssignmentRepository
                .findByTeacher_UserId(teacher.getUserId())
                .stream()
                .map(assignment -> new TeacherAssignmentResponse(
                        assignment.getAssignmentId(),

                        assignment.getTeacher().getUserId(),
                        assignment.getTeacher().getName(),

                        assignment.getClassEntity().getClassId(),
                        assignment.getClassEntity().getClassName(),

                        assignment.getSubject().getSubjectId(),
                        assignment.getSubject().getSubjectName(),
                        assignment.getSubject().getSubjectCode()
                ))
                .toList();
    }
    public List<TeacherAssignmentResponse> filter(
            Integer teacherId,
            Integer classId,
            Integer subjectId) {

        List<TeacherAssignment> assignments;

        if (teacherId != null &&
                classId != null &&
                subjectId != null) {

            assignments =
                    teacherAssignmentRepository
                            .findAllByTeacher_UserIdAndClassEntity_ClassIdAndSubject_SubjectId(
                                    teacherId,
                                    classId,
                                    subjectId
                            );

        } else if (teacherId != null &&
                classId != null) {

            assignments =
                    teacherAssignmentRepository
                            .findByTeacher_UserIdAndClassEntity_ClassId(
                                    teacherId,
                                    classId
                            );

        } else if (teacherId != null &&
                subjectId != null) {

            assignments =
                    teacherAssignmentRepository
                            .findByTeacher_UserIdAndSubject_SubjectId(
                                    teacherId,
                                    subjectId
                            );

        } else if (classId != null &&
                subjectId != null) {

            assignments =
                    teacherAssignmentRepository
                            .findByClassEntity_ClassIdAndSubject_SubjectId(
                                    classId,
                                    subjectId
                            );

        } else if (teacherId != null) {

            assignments =
                    teacherAssignmentRepository
                            .findByTeacher_UserId(teacherId);

        } else if (classId != null) {

            assignments =
                    teacherAssignmentRepository
                            .findByClassEntity_ClassId(classId);

        } else if (subjectId != null) {

            assignments =
                    teacherAssignmentRepository
                            .findBySubject_SubjectId(subjectId);

        } else {

            assignments =
                    teacherAssignmentRepository.findAll();
        }

        return assignments
                .stream()
                .map(assignment -> new TeacherAssignmentResponse(
                        assignment.getAssignmentId(),

                        assignment.getTeacher().getUserId(),
                        assignment.getTeacher().getName(),

                        assignment.getClassEntity().getClassId(),
                        assignment.getClassEntity().getClassName(),

                        assignment.getSubject().getSubjectId(),
                        assignment.getSubject().getSubjectName(),
                        assignment.getSubject().getSubjectCode()
                ))
                .toList();
    }

}
