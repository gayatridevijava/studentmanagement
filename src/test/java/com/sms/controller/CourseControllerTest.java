package com.sms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.dto.CourseDTO;
import com.sms.exception.ResourceNotFoundException;
import com.sms.service.ICourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CourseController.class)
@DisplayName("CourseController Tests")
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ICourseService courseService;

    @Autowired
    private ObjectMapper objectMapper;



    private CourseDTO testResponse;
    private CourseDTO testRequest;

    @BeforeEach
    void setUp() {
        testResponse = CourseDTO.builder()
                .id(10000)
                .name("Course1")
                .description("C1")
                .credits(3)
                .enrolledStudents(new ArrayList<>())
                .build();

        testRequest = CourseDTO.builder()
                .name("Course1")
                .description("C1")
                .credits(3)
                .enrolledStudents(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("GET /api/course - Should return all active courses")
    void testGetAllCoursesShouldReturnOk() throws Exception {
        when(courseService.getAllCourses()).thenReturn(List.of(testResponse));

        mockMvc.perform(get("/api/course"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Course1"));
    }

    @Test
    @DisplayName("GET /api/course/{id} - Should return student")
    void testGetCourseByIdShouldReturnOk() throws Exception {
        when(courseService.getCourseById(10000)).thenReturn(testResponse);

        mockMvc.perform(get("/api/course/10000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10000))
                .andExpect(jsonPath("$.name").value("Course1"));
    }

    @Test
    @DisplayName("GET /api/course/{id} - Should return 404 when not found")
    void testGetCourseByIdWhenNotFoundShouldReturn404() throws Exception {
        when(courseService.getCourseById(11)).thenThrow(new ResourceNotFoundException("Course", "courseId","11"));

        mockMvc.perform(get("/api/course/11"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/course - Should create course")
    void testCreateCourseShouldReturnCreated() throws Exception {
        when(courseService.createCourse(any())).thenReturn(testResponse);

        mockMvc.perform(post("/api/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /api/course - Should return 400 on invalid data")
    void testCreateStudentWithInvalidDataShouldReturn400() throws Exception {
        CourseDTO invalid = CourseDTO.builder()
                .name("").build();

        mockMvc.perform(post("/api/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/course/{id} - Should update course")
    void testUpdateCourseShouldReturnOk() throws Exception {
        when(courseService.updateCourse(eq(10000), any())).thenReturn(testResponse);

        mockMvc.perform(put("/api/course/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/course/{id} - Should delete course")
    void testDeleteCourseShouldReturnNoContent() throws Exception {
        doNothing().when(courseService).deleteCourse(10000);

        mockMvc.perform(delete("/api/course/1"))
                .andExpect(status().isNoContent());
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
