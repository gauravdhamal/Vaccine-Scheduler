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
    private Integer appointmentDetailsId;
    private LocalDate appointmentDate;
    private String appointmentTime;
    private LocalDateTime createdAt;
    private Boolean vaccinated;
//    private Person doctor;
    private Integer doctorId;
    private String doctorFirstName;
    private String doctorSpecialization;
//    private Vaccine vaccine;
    private Integer vaccineId;
    private String vaccineName;
//    private Person patient;
    private Integer patientId;
    private String patientFirstName;
    private String patientUsername;
    private String patientAadhaarNumber;
//    private PaymentDetail paymentDetail;
    private Integer paymentDetailPaymentId;
    private String paymentDetailTransactionStatus;
    private Integer paymentDetailAmount;
}
