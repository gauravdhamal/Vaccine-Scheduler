package com.vaccinescheduler.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VaccinationResponse {
    private Integer vaccinationDetailId;
    /**
     * It is equal to appointment date
     */
    private LocalDate vaccinatedDate;
    /**
     * It is equal to the endTime for particular slot.
     */
    private LocalTime vaccinatedTime;
    /**
     * It should be true
     */
    private Boolean vaccinationStatus;
    private String doseNumber;
    /**
     * After taking vaccine it'll be calculated on basis of vaccinatedDate + daysBetweenDoses = nextDate.
     */
    private LocalDate nextVaccinationDate;
    private String vaccineName;
    private String patientAadhaarNumber;
    private String patientAddressPhone;
    private String hospitalName;
}
