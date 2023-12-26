package com.vaccinescheduler.dtos.request;

import com.vaccinescheduler.models.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HospitalRequest {
    @NotNull(message = "hospitalName must not be null.")
    @NotEmpty(message = "hospitalName must not be empty.")
    @NotBlank(message = "hospitalName must not be blank.")
    private String hospitalName;

    @NotNull(message = "hospitalPinCode must not be null.")
    @NotEmpty(message = "hospitalPinCode must not be empty.")
    @NotBlank(message = "hospitalPinCode must not be blank.")
    private String hospitalPinCode;

    @NotNull(message = "operatingHours must not be null.")
    @NotEmpty(message = "operatingHours must not be empty.")
    @NotBlank(message = "operatingHours must not be blank.")
    private String operatingHours;

    @NotNull(message = "hospitalType must not be null.")
    @NotEmpty(message = "hospitalType must not be empty.")
    @NotBlank(message = "hospitalType must not be blank.")
    private String hospitalType;

    @Valid
    private Address address;
}
