package com.sms.service.impl;

import com.sms.dto.CourseDTO;
import com.sms.entity.Course;
import com.sms.entity.Enrollment;
import com.sms.exception.ResourceNotFoundException;
import com.sms.mapper.CourseMapper;
import com.sms.repository.CourseRepository;
import com.sms.repository.StudentEnrollmentRepository;
import com.sms.service.ICourseService;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseServiceImpl implements ICourseService {
    @Autowired
    CourseRepository courseRepository;

    @Autowired
    StudentEnrollmentRepository studentEnrollmentRepository;

    @Override
    public @Nullable List<CourseDTO> getAllCourses() {
        List<CourseDTO> coursList = new ArrayList<>();
       courseRepository.findAllByStatusEquals(Course.Status.Active).forEach(course -> {
                   coursList.add(CourseMapper.mapToCourseDTO(course, new CourseDTO()));
               });
        return coursList;
    }

    @Override
    public CourseDTO getCourseById(Integer id) {
        Course course = courseRepository.findByCourseIdAndStatusEquals(id, Course.Status.Active).orElseThrow(
                () -> new ResourceNotFoundException("Course","courseId",id.toString())
        );
        List<Enrollment> enrollments = studentEnrollmentRepository.findByCourse_CourseId(id);
        CourseDTO courseDTO = CourseMapper.mapToCourseDTO(course,new CourseDTO());
        return CourseMapper.mapCourseEnrollments(courseDTO,enrollments);

    }

    @Override
    @Transactional
    public CourseDTO createCourse(CourseDTO request) {
        Course course =  CourseMapper.mapToCourse(request,new Course());
       return CourseMapper.mapToCourseDTO(courseRepository.save(course),new CourseDTO());
    }

    @Override
    @Transactional
    public @Nullable CourseDTO updateCourse(Integer id, CourseDTO request) {
        Course course = courseRepository.findByCourseIdAndStatusEquals(id, Course.Status.Active).orElseThrow(
                () -> new ResourceNotFoundException("Course","courseId",id.toString())
        );
        course = courseRepository.save( CourseMapper.mapToCourse(request,course));
        return  CourseMapper.mapToCourseDTO(course,new CourseDTO());
    }

    @Override
    @Transactional
    public void deleteCourse(Integer id) {
        Course course = courseRepository.findByCourseIdAndStatusEquals(id, Course.Status.Active).orElseThrow(
                () -> new ResourceNotFoundException("Course","courseId",id.toString())
        );
        course.setStatus(Course.Status.Inactive);
        courseRepository.save(course);
    }
}
