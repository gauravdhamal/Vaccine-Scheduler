package com.vaccinescheduler.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VaccineResponse {
    private Integer vaccineId;
    private String vaccineName;
    private String vaccineManufacturer;
    private LocalDate manufacturedDate;
    private LocalDate expirationDate;
    private Double originalPrice;
    private String ageRange;
    private String discount;
    private Double discountedPrice;
    private Integer dosesRequired;
    private Integer daysBetweenDoses;
    private Boolean boosterDose;
}
