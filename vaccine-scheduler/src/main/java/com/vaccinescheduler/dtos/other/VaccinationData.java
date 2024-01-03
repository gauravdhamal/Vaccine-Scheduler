package com.vaccinescheduler.dtos.other;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@CsvRecord(separator = ",", skipFirstLine = true)
public class VaccinationData {
    @DataField(name = "patientId", pos = 1)
    private Integer patientId;
    @DataField(name = "vaccinationDetailId", pos = 2)
    private Integer vaccinationDetailId;
    @DataField(name = "patientName", pos = 3)
    private String patientName;
    @DataField(name = "patientAadhaarNumber", pos = 4)
    private String patientAadhaarNumber;
    @DataField(name = "patientEmail", pos = 5)
    private String patientEmail;
    @DataField(name = "patientPhone", pos = 6)
    private String patientPhone;
    @DataField(name = "vaccinatedDate", pos = 7)
    private LocalDate vaccinatedDate;
    @DataField(name = "vaccinatedTime", pos = 8)
    private String vaccinatedTime;
    @DataField(name = "vaccineName", pos = 9)
    private String vaccineName;
    @DataField(name = "vaccineDoseNumber", pos = 10)
    private String vaccineDoseNumber;
    @DataField(name = "nextVaccinationDate", pos = 11)
    private LocalDate nextVaccinationDate;
    @DataField(name = "hospitalName", pos = 12)
    private String hospitalName;
}
