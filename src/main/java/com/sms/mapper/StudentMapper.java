package com.sms.mapper;

import com.sms.dto.CourseDTO;
import com.sms.dto.StudentDTO;
import com.sms.entity.Course;
import com.sms.entity.Enrollment;
import com.sms.entity.Student;

public class StudentMapper {
    public static Student mapToStudent(StudentDTO dto, Student entity) {
       entity.setFirstName(dto.getFirstName());
       entity.setLastName(dto.getLastName());
       entity.setEmail(dto.getEmail());
       entity.setPhone(dto.getPhoneNumber());
       entity.setAddress(dto.getAddress());
       if(dto.getEnrolledCourses()!=null && !dto.getEnrolledCourses().isEmpty()){
           entity.getEnrollments().clear();

           if(null!=entity.getEnrollments()) {
             entity.getEnrollments().addAll(dto.getEnrolledCourses().stream().map(courseDTO ->
                     StudentMapper.mapToEnrollment(entity,CourseMapper.mapToCourse(courseDTO,new Course()),new Enrollment())).toList());
         }

       }
       return entity;

    }

    public static StudentDTO mapToStudentDTO(Student entity,StudentDTO dto) {
        dto.setFirstName( entity.getFirstName());
        dto.setLastName( entity.getLastName());
        dto.setEmail( entity.getEmail());
        dto.setPhoneNumber(entity.getPhone() );
        dto.setAddress(entity.getAddress());
        dto.setId(entity.getStudentId());
        dto.setEnrolledCourses(entity.getEnrollments().stream().map(
                e -> CourseMapper.mapToCourseDTO(e.getCourse(),new CourseDTO())).toList());

        return  dto;
    }

    public static Enrollment mapToEnrollment(Student student, Course course, Enrollment enrollment) {
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        return enrollment;


    }
}
