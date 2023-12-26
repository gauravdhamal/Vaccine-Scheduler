package com.vaccinescheduler.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.validation.constraints.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @NotNull(message = "city must not be null.")
    @NotEmpty(message = "city must not be empty.")
    @NotBlank(message = "city must not be blank.")
    private String city;

    @Pattern(regexp = "[6789]\\d{9}", message = "Invalid phone number. Must be a valid Indian mobile number having 10 digits.")
    private String phone;

    @Email(message = "Invalid email address.")
    private String email;
}
