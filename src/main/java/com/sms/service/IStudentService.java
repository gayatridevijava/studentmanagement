package com.sms.service;

import com.sms.dto.StudentDTO;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface IStudentService {
    @Nullable List<StudentDTO> getAllStudents();

    @Nullable StudentDTO getStudentById(Integer id);

    StudentDTO createStudent(@Valid StudentDTO request);

    @Nullable StudentDTO updateStudent(Integer id, @Valid StudentDTO request);

    StudentDTO enrollInCourse(Integer studentId, Integer courseId);

    StudentDTO unenrollFromCourse(Integer studentId, Integer courseId);

    void deleteStudent(Integer id);
}
