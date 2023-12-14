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
public class AppointmentDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer appointmentDetailsId;

    private LocalDateTime appointmentDateTime;
    private LocalDateTime createdAt;
    private Boolean vaccinated;

    /**
     * Uni-directional
     * For information about doctor.
     */
    @OneToOne
    @JoinColumn(name = "doctorId")
    private Person doctor;

    /**
     * Uni-directional
     * For information about doctor
     */
    @OneToOne
    @JoinColumn(name = "vaccineId")
    private Vaccine vaccine;

    /**
     * Bidirectional
     * "Many" appointments can belong to "One" hospital.
     * This is the owning side for relationship between Hospital and AppointmentDetails.
     * The reason for this field is if we want to get all appointments from hospital.
     */
    @ManyToOne
    @JoinColumn(name = "hospitalId")
    private Hospital hospital;

    /**
     * Bidirectional
     * This is the owning side for relationship between patient and appointmentDetails.
     */
    @ManyToOne
    @JoinColumn(name = "personId")
    private Person patient;

    /**
     * Bidirectional
     * This is the owning side for the relationship between paymentDetails and appointmentDetails.
     */
    @OneToOne
    @JoinColumn(name = "paymentId")
    private PaymentDetails paymentDetails;
}
