package com.vaccinescheduler.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDetailRequest {
    @NotNull(message = "firstName must not null.")
    @NotEmpty(message = "firstName must not empty.")
    @NotBlank(message = "firstName must not blank.")
    private String firstName;

    /**
     * Self, Other
     */
    @NotNull(message = "bookingFor must not null.")
    @NotEmpty(message = "bookingFor must not empty.")
    @NotBlank(message = "bookingFor must not blank.")
    @Pattern(regexp = "^(?i)(self|other)$", message = "Invalid bookingFor. Must be 'self', or 'other'.")
    private String bookingFor;

    /**
     * "first", "second", "third", "booster"
     */
    @NotNull(message = "doseNumber must not null.")
    @NotEmpty(message = "doseNumber must not empty.")
    @NotBlank(message = "doseNumber must not blank.")
    @Pattern(regexp = "^(?i)(first|second|third|booster)$", message = "Invalid doseNumber. Must be 'first', 'second', 'third' or 'booster'.")
    private String doseNumber;

    @NotNull(message = "gender must not null.")
    @NotEmpty(message = "gender must not empty.")
    @NotBlank(message = "gender must not blank.")
    @Pattern(regexp = "^(?i)(male|female|transgender)$", message = "Invalid gender. Must be 'male', 'female', or 'transgender'.")
    private String gender;

    @NotNull(message = "age must not be null.")
    @Min(value = 1, message = "Age must be greater than 1.")
    private Integer age;

    @Pattern(regexp = "[6789]\\d{9}", message = "Invalid phone number. Must be a valid Indian mobile number having 10 digits. And starts with '6' or '7' or '8' or '9'")
    private String phone;

    @Email(message = "Invalid email address.")
    private String email;
}
