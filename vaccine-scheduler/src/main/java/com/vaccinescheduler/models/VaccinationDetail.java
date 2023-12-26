package com.vaccinescheduler.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VaccinationDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer VaccinationDetailId;

    private LocalDate vaccinatedDate;
    private LocalTime vaccinatedTime;
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
     * "Many" vaccination can belong to "One" patient.
     * This is the owning side for relationship between patient and VaccinationDetail.
     */
    @ManyToOne
    @JoinColumn(name = "patientId")
    private Person patient;

    /**
     * Bidirectional
     * "Many" appointments can belong to "One" hospital.
     */
    @ManyToOne
    @JoinColumn(name = "hospitalId")
    private Hospital hospital;
}
