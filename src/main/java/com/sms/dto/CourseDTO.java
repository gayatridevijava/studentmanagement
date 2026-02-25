package com.sms.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class CourseDTO{
        private Integer id;

        @NotBlank(message = "Course name cannot be empty")
        private String name;

        private Integer credits;

        @NotBlank(message = "Course description cannot be empty")
        private String description;

        private List<StudentDTO> enrolledStudents;
}
