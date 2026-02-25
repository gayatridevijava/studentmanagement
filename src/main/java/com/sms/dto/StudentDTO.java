package com.sms.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDTO {
        private Integer id;
        @NotBlank(message = "Student First name cannot be empty")
        private String firstName;
        @NotBlank(message = "Student Last name cannot be empty")
        private String lastName;

        @NotBlank(message = "Student Email cannot be empty")
        @Email(message = "Invalid Email")
        private String email;

        @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
        private String phoneNumber;

        @NotBlank(message = "Student address cannot be empty")
        private String address;

        private List<CourseDTO> enrolledCourses;


}
