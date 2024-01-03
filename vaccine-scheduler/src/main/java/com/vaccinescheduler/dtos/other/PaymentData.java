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
public class PaymentData {
    private String patientName;
    private String patientGender;
    private Integer patientAge;
    private String patientCity;
    private String patientPhone;
    private String patientEmail;
    private String hospitalName;
    private Double paidAmount;
    private String paymentMethod;
    private Boolean notified;
}
