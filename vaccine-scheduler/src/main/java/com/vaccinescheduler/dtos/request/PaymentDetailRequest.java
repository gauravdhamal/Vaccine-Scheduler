package com.vaccinescheduler.dtos.request;

import com.vaccinescheduler.models.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PaymentDetailRequest {
    private String lastName;
    private LocalDate dateOfBirth;
    @Column(unique = true)
    private String aadhaarNumber;
    private Address address;
    @Column(unique = true)
    private String username;
    private String password;
    private Double amount;
    private String paymentMethod;
}
