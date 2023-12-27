package com.vaccinescheduler.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VaccineRequest {
    @NotNull(message = "vaccineName must not be null.")
    @NotEmpty(message = "vaccineName must not be empty.")
    @NotBlank(message = "vaccineName must not be blank.")
    private String vaccineName;

    @NotNull(message = "vaccineManufacturer must not be null.")
    @NotEmpty(message = "vaccineManufacturer must not be empty.")
    @NotBlank(message = "vaccineManufacturer must not be blank.")
    private String vaccineManufacturer;

    @NotNull(message = "manufacturedDate must not be null.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate manufacturedDate;

    @NotNull(message = "Original price must not be null.")
    @DecimalMin(value = "0.0", message = "Original price must be greater than or equal to 0.0.")
    private Double originalPrice;

    @NotNull(message = "Minimum age must not be null.")
    @Min(value = 0, message = "Minimum age must be greater than or equal to 0.")
    @Max(value = 99, message = "Minimum age must be less than or equal to 99.")
    private Integer minAge;

    @NotNull(message = "Maximum age must not be null.")
    @Min(value = 0, message = "Maximum age must be greater than or equal to 0.")
    @Max(value = 99, message = "Maximum age must be less than or equal to 99.")
    private Integer maxAge;

    @NotNull(message = "discount must not be null.")
    @NotEmpty(message = "discount must not be empty.")
    @NotBlank(message = "discount must not be blank.")
    private String discount;

    @Min(value = 1, message = "dosesRequired must be greater than equal to 1.")
    @Max(value = 3, message = "dosesRequired must be less than equal to 3.")
    private Integer dosesRequired;

    @Min(value = 30, message = "daysBetweenDoses must be greater than equal to 30.")
    @Max(value = 90, message = "daysBetweenDoses must be less than equal to 90.")
    private Integer daysBetweenDoses;

    @NotNull(message = "boosterDose must not null.")
    private Boolean boosterDose;
}
