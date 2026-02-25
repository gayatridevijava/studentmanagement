package com.sms.service;

import com.sms.dto.CourseDTO;
import com.sms.entity.Course;
import com.sms.entity.Enrollment;
import com.sms.exception.ResourceNotFoundException;
import com.sms.repository.CourseRepository;
import com.sms.repository.StudentEnrollmentRepository;
import com.sms.service.impl.CourseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentEnrollmentRepository studentEnrollmentRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    private Course course;
    private CourseDTO courseDTO;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setCourseId(1);
        course.setName("Math 101");
        course.setDescription("Math 101");
        course.setCredits(3);
        course.setStatus(Course.Status.Active);
        course.setEnrollments(new ArrayList<>());

        courseDTO = new CourseDTO();
        courseDTO.setId(1);
        courseDTO.setName("Math 101");
        courseDTO.setDescription("Math 101");
        courseDTO.setCredits(3);
        course.setEnrollments(new ArrayList<>());

    }

    @Test
    void testGetAllCoursesForAllActiveCourses() {
        when(courseRepository.findAllByStatusEquals(Course.Status.Active))
                .thenReturn(List.of(course));
        List<CourseDTO> result = courseService.getAllCourses();
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(courseRepository).findAllByStatusEquals(Course.Status.Active);
    }

    @Test
    void testFindCourseById() {
        when(courseRepository.findByCourseIdAndStatusEquals(1, Course.Status.Active))
                .thenReturn(Optional.of(course));

        when(studentEnrollmentRepository.findByCourse_CourseId(1))
                .thenReturn(List.of(new Enrollment()));
            CourseDTO result = courseService.getCourseById(1);
            assertNotNull(result);
            verify(courseRepository).findByCourseIdAndStatusEquals(1, Course.Status.Active);
            verify(studentEnrollmentRepository).findByCourse_CourseId(1);

    }

    @Test
    void testGetCourseByIdForExceptionWhenCourseNotFound() {

        when(courseRepository.findByCourseIdAndStatusEquals(1, Course.Status.Active))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> courseService.getCourseById(1));
    }

    @Test
    void testCreateCourse() {
        when(courseRepository.save(any(Course.class)))
                .thenReturn(course);
            CourseDTO result = courseService.createCourse(courseDTO);
            assertNotNull(result);
            verify(courseRepository).save(any(Course.class));

    }

    @Test
    void testUpdateStudentForSuccess() {
        when(courseRepository.findByCourseIdAndStatusEquals(1, Course.Status.Active))
                .thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class)))
                .thenReturn(course);
            CourseDTO result = courseService.updateCourse(1, courseDTO);
            assertNotNull(result);
            verify(courseRepository).save(any(Course.class));
    }

    @Test
    void testUpdateCourseForExceptionWhenCourseNotFound() {
        when(courseRepository.findByCourseIdAndStatusEquals(1, Course.Status.Active))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> courseService.updateCourse(1, courseDTO));
    }

    @Test
    void testDeleteCourseForSoftDelete() {
        when(courseRepository.findByCourseIdAndStatusEquals(1, Course.Status.Active))
                .thenReturn(Optional.of(course));

        courseService.deleteCourse(1);
        assertEquals(Course.Status.Inactive, course.getStatus());
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void testDeleteCourseForExceptionWhenCourseNotFound() {
        when(courseRepository.findByCourseIdAndStatusEquals(1, Course.Status.Active))
                .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> courseService.deleteCourse(1));
    }
}