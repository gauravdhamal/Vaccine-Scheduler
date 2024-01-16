package com.vaccinescheduler.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AppointmentDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer appointmentDetailId;

    private LocalDate appointmentDate;
    private String appointmentTime;
    private LocalDateTime createdAt;
    private String doseNumber;
    private String bookingFor;
    private String firstName;
    private String gender;
    private Integer age;
    private String phone;
    private String email;
    private Boolean vaccinated;
    private LocalDate dateOfBirth;

    /**
     * Bidirectional
     * For information about doctor.
     */
    @ManyToOne
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
     * "Many" appointments can belong to "One" patient.
     * This is the owning side for relationship between patient and appointmentDetails.
     */
    @ManyToOne
    @JoinColumn(name = "patientId")
    private Person patient;

    /**
     * Bidirectional
     * This is the owning side for the relationship between paymentDetails and appointmentDetails.
     */
    @OneToOne
    @JoinColumn(name = "paymentId")
    private PaymentDetail paymentDetail;

    /**
     * Bidirectional
     * "Many" appointments can belong to "One" hospital.
     */
    @ManyToOne
    @JoinColumn(name = "hospitalId")
    private Hospital hospital;

    /**
     * Uni-directional
     * "Many" appointment can have "One" slot.
     */
    @ManyToOne
    private Slot slot;
}
