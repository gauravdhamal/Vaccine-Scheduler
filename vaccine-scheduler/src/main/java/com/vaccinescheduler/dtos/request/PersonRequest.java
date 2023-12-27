package com.vaccinescheduler.dtos.request;

import com.vaccinescheduler.models.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonRequest {
    @NotNull(message = "firstName must not null.")
    @NotEmpty(message = "firstName must not empty.")
    @NotBlank(message = "firstName must not blank.")
    private String firstName;

    @NotNull(message = "lastName must not null.")
    @NotEmpty(message = "lastName must not empty.")
    @NotBlank(message = "lastName must not blank.")
    private String lastName;

    @Past(message = "DateOfBirth must be in past.")
    @NotNull(message = "dateOfBirth must not null.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfBirth;

    @NotNull(message = "gender must not null.")
    @NotEmpty(message = "gender must not empty.")
    @NotBlank(message = "gender must not blank.")
    @Pattern(regexp = "^(?i)(male|female|transgender)$", message = "Invalid gender. Must be 'male', 'female', or 'transgender'.")
    private String gender;

    @NotNull(message = "role must not be null.")
    @NotEmpty(message = "role must not be empty.")
    @NotBlank(message = "role must not be blank.")
    @Pattern(regexp = "^(?i)(admin|patient|doctor)$", message = "Invalid role. Must be 'admin', 'patient', or 'doctor'.")
    private String role;

    @Pattern(regexp = "^(vaccinator|null)$", message = "Invalid specialization. Must be 'null', or 'vaccinator'.")
    private String specialization;

    @NotNull(message = "age must not be null.")
    @NotEmpty(message = "age must not be empty.")
    @NotBlank(message = "age must not be blank.")
    private Integer age;

    @NotNull(message = "aadhaarNumber must not be null.")
    @NotEmpty(message = "aadhaarNumber must not be empty.")
    @NotBlank(message = "aadhaarNumber must not be blank.")
    @Column(unique = true)
    private String aadhaarNumber;

    @Valid
    private Address address;

    @NotNull(message = "username must not be null.")
    @NotEmpty(message = "username must not be empty.")
    @NotBlank(message = "username must not be blank.")
    @Column(unique = true)
    private String username;

    @NotNull(message = "password must not be null.")
    @NotEmpty(message = "password must not be empty.")
    @NotBlank(message = "password must not be blank.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#%&])(?=.*\\S).{8,}$",
            message = "Password must contain at least 1 capital, 1 small, 1 special character, 1 number, having minimum length 8.")
    private String password;
}
