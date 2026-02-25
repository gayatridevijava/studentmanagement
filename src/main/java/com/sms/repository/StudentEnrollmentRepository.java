package com.sms.repository;

import com.sms.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentEnrollmentRepository extends JpaRepository<Enrollment,Integer> {

    List<Enrollment> findByCourse_CourseId(Integer courseId);


    List<Enrollment> deleteByStudent_StudentId(Integer studentId);

}
