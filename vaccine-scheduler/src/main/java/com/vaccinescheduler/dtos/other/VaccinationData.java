package com.vaccinescheduler.dtos.other;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VaccinationData {
    private String patientName;
    private String patientAadhaarNumber;
    private String patientEmail;
    private String patientPhone;
    private LocalDate vaccinatedDate;
    private String vaccinatedTime;
    private String vaccineName;
    private String vaccineDoseNumber;
    private LocalDate nextVaccinationDate;
    private String hospitalName;
}
