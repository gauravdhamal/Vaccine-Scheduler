package com.vaccinescheduler.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDetailResponse {
    private Integer appointmentDetailsId;
    private LocalDate appointmentDate;
    private String appointmentTime;
    private LocalDateTime createdAt;
    private Boolean vaccinated;
    private String message;
//    private Person doctor;
    private Integer doctorId;
    private String doctorUsername;
//    private Vaccine vaccine;
    private Integer vaccineId;
    private String vaccineName;
    private Double vaccineDiscountedPrice;
//    private Person patient;
    private Integer patientId;
    private String patientFirstName;
    private String patientLastName;
    private String patientAadhaarNumber;
//    private PaymentDetail paymentDetail;
    private Integer paymentDetailId;
    private Integer paymentDetailAmount;
}
