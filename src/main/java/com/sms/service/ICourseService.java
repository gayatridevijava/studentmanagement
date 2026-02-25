package com.sms.service;

import com.sms.dto.CourseDTO;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface ICourseService {
    @Nullable List<CourseDTO> getAllCourses();

    CourseDTO getCourseById(Integer id);

    CourseDTO createCourse(CourseDTO request);

    @Nullable CourseDTO updateCourse(Integer id, CourseDTO request);

    void deleteCourse(Integer id);
}
