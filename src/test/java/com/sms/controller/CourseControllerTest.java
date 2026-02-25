package com.sms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.dto.StudentDTO;
import com.sms.exception.ResourceNotFoundException;
import com.sms.service.IStudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;




@WebMvcTest(StudentController.class)
@DisplayName("StudentController Tests")
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IStudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;



    private StudentDTO testResponse;
    private StudentDTO testRequest;

    @BeforeEach
    void setUp() {
        testResponse = StudentDTO.builder()
                .id(10000)
                .firstName("John")
                .lastName("Doe")
                .email("john@test.com")
                .enrolledCourses(new ArrayList<>())
                .build();

        testRequest = StudentDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@test.com")
                .phoneNumber("1234567890")
                .address("Test Address")
                .enrolledCourses(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("GET /api/students - Should return all active students")
    void testGetAllStudentsShouldReturnOk() throws Exception {
        when(studentService.getAllStudents()).thenReturn(List.of(testResponse));

        mockMvc.perform(get("/api/student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("john@test.com"));
    }

    @Test
    @DisplayName("GET /api/student/{id} - Should return student")
    void testGetStudentByIdShouldReturnOk() throws Exception {
        when(studentService.getStudentById(10000)).thenReturn(testResponse);

        mockMvc.perform(get("/api/student/10000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10000))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    @DisplayName("GET /api/students/{id} - Should return 404 when not found")
    void testGetStudentByIdWhenNotFoundShouldReturn404() throws Exception {
        when(studentService.getStudentById(11)).thenThrow(new ResourceNotFoundException("Student", "studentId","11"));

        mockMvc.perform(get("/api/student/11"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/student - Should create student")
    void testCreateStudentShouldReturnCreated() throws Exception {
        when(studentService.createStudent(any())).thenReturn(testResponse);

        mockMvc.perform(post("/api/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /api/students - Should return 400 on invalid data")
    void testCreateStudentWithInvalidDataShouldReturn400() throws Exception {
        StudentDTO invalid = StudentDTO.builder()
                .firstName("").email("not-an-email").build();

        mockMvc.perform(post("/api/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/student/{id} - Should update student")
    void testUpdateStudentShouldReturnOk() throws Exception {
        when(studentService.updateStudent(eq(10000), any())).thenReturn(testResponse);

        mockMvc.perform(put("/api/student/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/students/{id} - Should delete student")
    void testDeleteStudentShouldReturnNoContent() throws Exception {
        doNothing().when(studentService).deleteStudent(10000);

        mockMvc.perform(delete("/api/student/1"))
                .andExpect(status().isOk());
    }


    @TestConfiguration
    static class ObjectMapperTestConfig {
        @Bean
        public ObjectMapper objectMapper() {
            // Configure the mapper as needed
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules(); // Ensures modules are found
            return mapper;
        }
    }
}
