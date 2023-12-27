package com.vaccinescheduler.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
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
