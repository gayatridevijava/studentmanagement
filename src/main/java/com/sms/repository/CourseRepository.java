package com.sms.repository;


import com.sms.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
   List<Course> findAllByStatusEquals(Course.Status status);
   Optional<Course> findByCourseIdAndStatusEquals(Integer id, Course.Status status);

}
