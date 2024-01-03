package com.vaccinescheduler.dtos.other;

import lombok.*;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentData {
    private String patientName;
    private Integer patientAge;
    private String patientGender;
    private String patientPhone;
    private String patientEmail;
    private String HospitalName;
    private String HospitalCity;
    private String HospitalContact;
    private String VaccineName;
    private String DoctorName;
    private String AppointmentDate;
    private String AppointmentTime;
    private Boolean notified;
}
