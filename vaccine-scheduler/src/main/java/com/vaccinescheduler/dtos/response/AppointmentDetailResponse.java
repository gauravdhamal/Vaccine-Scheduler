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
    private Integer appointmentDetailId;
    private LocalDate appointmentDate;
    private String appointmentTime;
    private String message;
    private Integer paymentDetailId;
    private Integer paymentDetailAmount;
}
