package com.sms.service.impl;

import com.sms.dto.StudentDTO;
import com.sms.entity.Course;
import com.sms.entity.Enrollment;
import com.sms.entity.Student;
import com.sms.exception.ResourceNotFoundException;
import com.sms.mapper.StudentMapper;
import com.sms.repository.CourseRepository;
import com.sms.repository.StudentEnrollmentRepository;
import com.sms.repository.StudentRepository;
import com.sms.service.IStudentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StudentServiceImpl implements IStudentService {

    private final StudentRepository studentRepository;
    private final StudentEnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;

    @Override
    public @Nullable List<StudentDTO> getAllStudents() {
        List<StudentDTO> students = new ArrayList<>();
        studentRepository.findAllByStatusEquals(Student.Status.Active).forEach(student -> {
            students.add(StudentMapper.mapToStudentDTO(student,new StudentDTO()));
        });
        return students;
    }

    @Override
    public @Nullable StudentDTO getStudentById(Integer id) {
       Student student = studentRepository.findByStudentIdAndStatusEquals(id, Student.Status.Active).orElseThrow(
               () -> new ResourceNotFoundException("Student", "studentId", id.toString())

       );
        return StudentMapper.mapToStudentDTO(student,new StudentDTO());
    }

    @Override
    @Transactional
    public StudentDTO createStudent(StudentDTO request) {
        Student student =  StudentMapper.mapToStudent(request,new Student());
        return StudentMapper.mapToStudentDTO(studentRepository.save(student),new StudentDTO());

    }

    @Override
    @Transactional
    public @Nullable StudentDTO updateStudent(Integer id, StudentDTO request) {
        Student student = studentRepository.findByStudentIdAndStatusEquals(id, Student.Status.Active).orElseThrow(
                () -> new ResourceNotFoundException("Student", "studentId", id.toString())

        );
        student.getEnrollments().clear();
        enrollmentRepository.deleteByStudent_StudentId(id);

        Student updatedStudent  = studentRepository.saveAndFlush(StudentMapper.mapToStudent(request,student));
        return StudentMapper.mapToStudentDTO(updatedStudent,new StudentDTO());
    }

    @Override
    @Transactional
    public StudentDTO enrollInCourse(Integer studentId, Integer courseId) {
        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new ResourceNotFoundException("Student", "studentId", studentId.toString())

        );
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new ResourceNotFoundException("Course", "courseId", courseId.toString())
        );

        enrollmentRepository.save(StudentMapper.mapToEnrollment(student,course,new Enrollment()));
        return null;
    }

    @Override
    @Transactional
    public StudentDTO unenrollFromCourse(Integer studentId, Integer courseId) {
        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new ResourceNotFoundException("Student", "studentId", studentId.toString())

        );
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new ResourceNotFoundException("Course", "courseId", courseId.toString())
        );
        enrollmentRepository.delete(
                StudentMapper.mapToEnrollment(student,course,new Enrollment())
        );

        return null;
    }

    @Override
    @Transactional
    public void deleteStudent(Integer id) {
        Student student = studentRepository.findByStudentIdAndStatusEquals(id, Student.Status.Active).orElseThrow(
                () -> new ResourceNotFoundException("Student", "studentId", id.toString())
        );
        student.getEnrollments().clear();
        enrollmentRepository.deleteByStudent_StudentId(id);
        student.setStatus(Student.Status.Inactive);
        studentRepository.save(student);
    }
}
