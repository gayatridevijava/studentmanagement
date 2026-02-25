package com.sms.mapper;

import com.sms.dto.CourseDTO;
import com.sms.dto.StudentDTO;
import com.sms.entity.Course;
import com.sms.entity.Enrollment;

import java.util.List;

public class CourseMapper {

    public static CourseDTO mapToCourseDTO(Course course,CourseDTO courseDTO) {
        courseDTO.setCredits(course.getCredits());
        courseDTO.setName(course.getName());
        courseDTO.setDescription(course.getDescription());
        courseDTO.setId(course.getCourseId());
        return courseDTO;
    }

    public static Course mapToCourse(CourseDTO courseDTO,Course course) {
        course.setCredits(courseDTO.getCredits());
        course.setName(courseDTO.getName());
        course.setDescription(courseDTO.getDescription());
        if(courseDTO.getId()!=null){
            course.setCourseId(courseDTO.getId());
        }
        return course;
    }

    public static CourseDTO mapCourseEnrollments(CourseDTO courseDTO, List<Enrollment> enrollments) {
        courseDTO.setEnrolledStudents(
                enrollments.stream().map(
                        e -> StudentMapper.mapToStudentDTO(e.getStudent(),new StudentDTO())
                ).toList()
        );
        return courseDTO;
    }
}
