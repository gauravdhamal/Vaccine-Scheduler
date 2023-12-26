package com.vaccinescheduler.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VaccineRequest {
    private String vaccineName;
    private String vaccineManufacturer;
    private LocalDate manufacturedDate;
    private Double originalPrice;
    private Integer minAge;
    private Integer maxAge;
    private String discount;
    private int dosesRequired;
    private int daysBetweenDoses;
    private Boolean boosterDose;
}
