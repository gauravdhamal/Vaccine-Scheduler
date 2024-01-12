package com.vaccinescheduler.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.Mapping;

import javax.persistence.Column;
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
    private String firstName;
    private String gender;
    private Integer age;
    private String phone;
    private String email;
    private String message;
    private Double vaccineDiscountedPrice;
    private Integer paymentDetailId;
    private Integer paymentDetailAmount;
}
