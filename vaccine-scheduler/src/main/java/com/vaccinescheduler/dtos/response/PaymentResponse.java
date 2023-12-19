package com.vaccinescheduler.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private Integer paymentId;
    private LocalDateTime createdDateTime;
    private Integer amount;
    private String paymentMethod;
    private String transactionStatus;
    private Integer patientId;
    private String patientFirstName;
    private String patientUsername;
    private Integer appointmentDetailId;
    private LocalDate appointmentDetailAppointmentDate;
    private String appointmentDetailAppointmentTime;
}
