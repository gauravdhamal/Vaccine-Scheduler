package com.vaccinescheduler.dtos.other;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@CsvRecord(separator = ",", skipFirstLine = true)
public class PaymentData {
    @DataField(name = "patientFirstName", pos = 1)
    private String patientName;
    @DataField(name = "patientGender", pos = 2)
    private String patientGender;
    @DataField(name = "patientAge", pos = 3)
    private Integer patientAge;
    @DataField(name = "patientCity", pos = 4)
    private String patientCity;
    @DataField(name = "patientPhone", pos = 5)
    private String patientPhone;
    @DataField(name = "patientEmail", pos = 6)
    private String patientEmail;
    @DataField(name = "hospitalName", pos = 7)
    private String hospitalName;
    @DataField(name = "paidAmount", pos = 8)
    private Double paidAmount;
    @DataField(name = "paymentMethod", pos = 9)
    private String paymentMethod;
    @DataField(name = "notified", pos = 10)
    private Boolean notified;
}
