package com.sms.service;

import com.sms.dto.StudentDTO;
import com.sms.entity.Course;
import com.sms.entity.Enrollment;
import com.sms.entity.Student;
import com.sms.exception.ResourceNotFoundException;
import com.sms.repository.CourseRepository;
import com.sms.repository.StudentEnrollmentRepository;
import com.sms.repository.StudentRepository;
import com.sms.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentEnrollmentRepository enrollmentRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student activeStudent;
    private Student inactiveStudent;
    private Course course;
    private Enrollment enrollment;
    private StudentDTO studentDTO;

    @BeforeEach
    void setUp() {
        activeStudent = new Student();
        activeStudent.setStudentId(1);
        activeStudent.setFirstName("John");
        activeStudent.setLastName("Doe");
        activeStudent.setEmail("john.doe@example.com");
        activeStudent.setStatus(Student.Status.Active);
        activeStudent.setEnrollments(new ArrayList<>());

        inactiveStudent = new Student();
        inactiveStudent.setStudentId(2);
        inactiveStudent.setStatus(Student.Status.Inactive);

        course = new Course();
        course.setCourseId(1);
        course.setName("Math 101");
        course.setStatus(Course.Status.Active);
        course.setEnrollments(new ArrayList<>());

        enrollment = new Enrollment();
        enrollment.setEnrollmentId(1);
        enrollment.setStudent(activeStudent);
        enrollment.setCourse(course);
        enrollment.setStatus(Enrollment.Status.Active);

        studentDTO = new StudentDTO();
        studentDTO.setFirstName("John");
        studentDTO.setLastName("Doe");
        studentDTO.setEmail("john.doe@example.com");
    }

    // ─── getAllStudents ───────────────────────────────────────────────────────

    @Test
    void testGetAllStudentsForActiveStudents() {
        when(studentRepository.findAllByStatusEquals(Student.Status.Active))
                .thenReturn(List.of(activeStudent));

        List<StudentDTO> result = studentService.getAllStudents();

        assertThat(result).isNotNull().hasSize(1);
        verify(studentRepository).findAllByStatusEquals(Student.Status.Active);
    }

    @Test
    void testGetAllStudentsForEmptyListWhenNoActiveStudents() {
        when(studentRepository.findAllByStatusEquals(Student.Status.Active))
                .thenReturn(List.of());

        List<StudentDTO> result = studentService.getAllStudents();

        assertThat(result).isNotNull().isEmpty();
    }

    // ─── getStudentById ───────────────────────────────────────────────────────

    @Test
    void testGetStudentByIdForStudentFound() {
        when(studentRepository.findByStudentIdAndStatusEquals(1, Student.Status.Active))
                .thenReturn(Optional.of(activeStudent));

        StudentDTO result = studentService.getStudentById(1);

        assertThat(result).isNotNull();
        verify(studentRepository).findByStudentIdAndStatusEquals(1, Student.Status.Active);
    }

    @Test
    void testGetStudentByIdForResourceNotFoundExceptionWhenNotFound() {
        when(studentRepository.findByStudentIdAndStatusEquals(99, Student.Status.Active))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.getStudentById(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testGetStudentByIdForResourceNotFoundExceptionForInactiveStudent() {
        when(studentRepository.findByStudentIdAndStatusEquals(2, Student.Status.Active))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.getStudentById(2))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // ─── createStudent ────────────────────────────────────────────────────────

    @Test
    void testCreateStudentForSuccess() {
        when(studentRepository.save(any(Student.class))).thenReturn(activeStudent);

        studentService.createStudent(studentDTO);

        verify(studentRepository).save(any(Student.class));
    }

    // ─── updateStudent ────────────────────────────────────────────────────────

    @Test
    void testUpdateStudentForSuccess() {
        activeStudent.setEnrollments(new ArrayList<>(List.of(enrollment)));

        when(studentRepository.findByStudentIdAndStatusEquals(1, Student.Status.Active))
                .thenReturn(Optional.of(activeStudent));
        when(studentRepository.saveAndFlush(any(Student.class))).thenReturn(activeStudent);

        StudentDTO result = studentService.updateStudent(1, studentDTO);

        assertThat(result).isNotNull();
        verify(enrollmentRepository).deleteByStudent_StudentId(1);
        verify(studentRepository).saveAndFlush(any(Student.class));
    }

    @Test
    void testUpdateStudentForResourceNotFoundExceptionWhenStudentNotFound() {
        when(studentRepository.findByStudentIdAndStatusEquals(99, Student.Status.Active))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.updateStudent(99, studentDTO))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(enrollmentRepository, never()).deleteByStudent_StudentId(any());
        verify(studentRepository, never()).saveAndFlush(any());
    }

    @Test
    void testUpdateStudentClearsExistingEnrollmentsBeforeSaving() {
        List<Enrollment> enrollments = new ArrayList<>(List.of(enrollment));
        activeStudent.setEnrollments(enrollments);

        when(studentRepository.findByStudentIdAndStatusEquals(1, Student.Status.Active))
                .thenReturn(Optional.of(activeStudent));
        when(studentRepository.saveAndFlush(any(Student.class))).thenReturn(activeStudent);

        studentService.updateStudent(1, studentDTO);

        verify(enrollmentRepository).deleteByStudent_StudentId(1);
        assertThat(activeStudent.getEnrollments()).isEmpty();
    }

    // ─── enrollInCourse ───────────────────────────────────────────────────────

    @Test
    void testEnrollInCourseForSaveEnrollmentSuccess() {
        when(studentRepository.findById(1)).thenReturn(Optional.of(activeStudent));
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);

        studentService.enrollInCourse(1, 1);

        verify(enrollmentRepository).save(any(Enrollment.class));
    }

    @Test
    void testEnrollInCourseForResourceNotFoundExceptionWhenStudentNotFound() {
        when(studentRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.enrollInCourse(99, 1))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void testEnrollInCourseForResourceNotFoundExceptionWhenCourseNotFound() {
        when(studentRepository.findById(1)).thenReturn(Optional.of(activeStudent));
        when(courseRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.enrollInCourse(1, 99))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(enrollmentRepository, never()).save(any());
    }

    // ─── unenrollFromCourse ───────────────────────────────────────────────────

    @Test
    void testUnenrollFromCourseForDeleteEnrollmentSuccess() {
        when(studentRepository.findById(1)).thenReturn(Optional.of(activeStudent));
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));

        studentService.unenrollFromCourse(1, 1);

        verify(enrollmentRepository).delete(any(Enrollment.class));
    }

    @Test
    void testUnenrollFromCourseForResourceNotFoundExceptionWhenStudentNotFound() {
        when(studentRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.unenrollFromCourse(99, 1))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(enrollmentRepository, never()).delete(any());
    }

    @Test
    void testUnenrollFromCourseForResourceNotFoundExceptionWhenCourseNotFound() {
        when(studentRepository.findById(1)).thenReturn(Optional.of(activeStudent));
        when(courseRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.unenrollFromCourse(1, 99))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(enrollmentRepository, never()).delete(any());
    }

    // ─── deleteStudent ────────────────────────────────────────────────────────

    @Test
    void testDeleteStudentForSetStatusToInactiveAndClearsEnrollments() {
        activeStudent.setEnrollments(new ArrayList<>(List.of(enrollment)));

        when(studentRepository.findByStudentIdAndStatusEquals(1, Student.Status.Active))
                .thenReturn(Optional.of(activeStudent));

        studentService.deleteStudent(1);

        assertThat(activeStudent.getStatus()).isEqualTo(Student.Status.Inactive);
        assertThat(activeStudent.getEnrollments()).isEmpty();
        verify(enrollmentRepository).deleteByStudent_StudentId(1);
        verify(studentRepository).save(activeStudent);
    }

    @Test
    void testDeleteStudentForResourceNotFoundExceptionWhenStudentNotFound() {
        when(studentRepository.findByStudentIdAndStatusEquals(99, Student.Status.Active))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.deleteStudent(99))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(enrollmentRepository, never()).deleteByStudent_StudentId(any());
        verify(studentRepository, never()).save(any());
    }

    @Test
    void testDeleteStudentDoesNotHardDelete() {
        when(studentRepository.findByStudentIdAndStatusEquals(1, Student.Status.Active))
                .thenReturn(Optional.of(activeStudent));

        studentService.deleteStudent(1);

        verify(studentRepository, never()).delete(any(Student.class));
        verify(studentRepository, never()).deleteById(any());
    }
}