package com.vaccinescheduler.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PaymentDetailRequest {
    private Integer amount;
    private String paymentMethod;
//    private Person patient;
    private Integer patientId;
//    private AppointmentDetail appointmentDetail;
    private Integer appointmentDetailId;
}
