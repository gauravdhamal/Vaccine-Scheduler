package com.vaccinescheduler.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer paymentId;

    private LocalDateTime createdDateTime;
    private Integer amount;
    /**
     * cash, card
     */
    private String paymentMethod;
    /**
     * success, failed
     */
    private String transactionStatus;

    /**
     * Uni-directional
     * "One" payment receipt belong to "One" patient.
     */
    @OneToOne
    private Person patient;

    /**
     * Bidirectional
     * "One" payment belongs to only "One" appointment.
     */
    @OneToOne(mappedBy = "paymentDetail", cascade = CascadeType.ALL)
    private AppointmentDetail appointmentDetail;
}
