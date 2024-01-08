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
public class SlotResponse {
    private Integer slotId;
    private LocalDate slotDate;
    private String slotTiming;
    private Integer availableSlots;
    private Integer doctorId;
    private String doctorFirstName;
    private String doctorUsername;
    private Integer vaccineId;
    private String vaccineName;
    private Double vaccineOriginalPrice;
    private String vaccineDiscount;
    private String requiredAgeRange;
    private Integer doctorHospitalHospitalId;
    private String doctorHospitalHospitalName;
}
