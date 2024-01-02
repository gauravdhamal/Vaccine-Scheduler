package com.vaccinescheduler.dtos.other;

import lombok.*;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@CsvRecord(separator = ",", skipFirstLine = true)
public class AppointmentData {
    @DataField(name = "patientName", pos = 1)
    private String patientName;
    @DataField(name = "patientAge", pos = 2)
    private Integer patientAge;
    @DataField(name = "patientGender", pos = 3)
    private String patientGender;
    @DataField(name = "patientPhone", pos = 4)
    private String patientPhone;
    @DataField(name = "patientEmail", pos = 5)
    private String patientEmail;
    @DataField(name = "HospitalName", pos = 6)
    private String HospitalName;
    @DataField(name = "HospitalCity", pos = 7)
    private String HospitalCity;
    @DataField(name = "HospitalContact", pos = 8)
    private String HospitalContact;
    @DataField(name = "VaccineName", pos = 9)
    private String VaccineName;
    @DataField(name = "DoctorName", pos = 10)
    private String DoctorName;
    @DataField(name = "AppointmentDate", pos = 11)
    private String AppointmentDate;
    @DataField(name = "AppointmentTime", pos = 12)
    private String AppointmentTime;
    @DataField(name = "notified", pos = 13)
    private Boolean notified;
}
