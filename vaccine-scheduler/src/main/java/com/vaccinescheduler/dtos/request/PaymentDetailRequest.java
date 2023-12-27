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

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PaymentDetailRequest {
    @NotNull(message = "lastName must not null.")
    @NotEmpty(message = "lastName must not empty.")
    @NotBlank(message = "lastName must not blank.")
    private String lastName;

    @Past(message = "DateOfBirth must be in past.")
    @NotNull(message = "dateOfBirth must not null.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfBirth;

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

    @NotNull(message = "Amount must not be null.")
    @DecimalMin(value = "0.0", message = "Amount must be greater than or equal to 0.0.")
    private Double amount;

    @NotNull(message = "paymentMethod must not null.")
    @NotEmpty(message = "paymentMethod must not empty.")
    @NotBlank(message = "paymentMethod must not blank.")
    @Pattern(regexp = "^(?i)(cash|card)$", message = "Invalid paymentMethod. Must be 'cash', or 'card'.")
    private String paymentMethod;
}
