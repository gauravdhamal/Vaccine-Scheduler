package com.vaccinescheduler.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    private Integer appointmentDetailId;
    private LocalDate appointmentDate;
    private String appointmentTime;
    private LocalDateTime createdAt;
    private String doseNumber;
    private Integer doctorId;
    private String doctorFirstName;
    private String doctorSpecialization;
    private Integer vaccineId;
    private String vaccineName;
    private Integer patientId;
    private String patientFirstName;
    private String patientUsername;
    private String patientAadhaarNumber;
    private Integer paymentDetailPaymentId;
    private String paymentDetailTransactionStatus;
    private Integer paymentDetailAmount;
}
