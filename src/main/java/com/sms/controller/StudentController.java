package com.sms.controller;

import com.sms.constants.SMSConstants;
import com.sms.dto.ResponseDTO;
import com.sms.dto.StudentDTO;
import com.sms.service.IStudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
@CrossOrigin
public class StudentController {

    private final IStudentService studentService;

    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Integer id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> createStudent(@Valid @RequestBody StudentDTO request) {
        studentService.createStudent(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDTO(SMSConstants.STATUS_201, SMSConstants.MESSAGE_201));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(
            @PathVariable Integer id, @Valid @RequestBody StudentDTO request) {
        return ResponseEntity.ok(studentService.updateStudent(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteStudent(@PathVariable Integer id) {
        studentService.deleteStudent(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(SMSConstants.STATUS_200, SMSConstants.MESSAGE_200));

    }

    @PostMapping("/{studentId}/courses/{courseId}")
    public ResponseEntity<StudentDTO> enrollInCourse(
            @PathVariable Integer studentId, @PathVariable Integer courseId) {
        return ResponseEntity.ok(studentService.enrollInCourse(studentId, courseId));
    }

    @DeleteMapping("/{studentId}/courses/{courseId}")
    public ResponseEntity<StudentDTO> unenrollFromCourse(
            @PathVariable Integer studentId, @PathVariable Integer courseId) {
        return ResponseEntity.ok(studentService.unenrollFromCourse(studentId, courseId));
    }
}
