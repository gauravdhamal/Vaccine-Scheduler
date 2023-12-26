package com.vaccinescheduler.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDetailResponse {
    private Integer paymentId;
    private LocalDateTime createdDateTime;
    private Double amount;
    private String paymentMethod;
    private String transactionStatus;
    private Integer patientId;
    private String patientFirstName;
    private String patientUsername;
}
