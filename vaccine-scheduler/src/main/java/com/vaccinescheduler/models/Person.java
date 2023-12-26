package com.vaccinescheduler.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer personId;

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String role;
    private String specialization;
    private Integer age;
    @Column(unique = true)
    private String aadhaarNumber;
    private Address address;
    @Column(unique = true)
    private String username;
    private String password;

    /**
     * Bidirectional
     * This field is for admin purpose.
     * "One" admin can be assigned to "One" inventory at a time.
     * Mapped by "admin" variable present in Inventory class.
     * Because inventory must know about the admin.
     */
    @OneToOne(mappedBy = "manager", cascade = CascadeType.ALL)
    private Inventory inventory;

    /**
     * Bidirectional
     * This field is for doctor purpose.
     * "Many" doctors can be assigned to "One" hospital at a time.
     * This is the owner field for "Hospital" class list of doctors.
     */
    @ManyToOne
    @JoinColumn(name = "hospitalId")
    private Hospital hospital;

    /**
     * Bidirectional
     * This field is for doctor purpose.
     * "One" doctor can have "Many" slots available per day.
     */
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<Slot> slots = new ArrayList<>();

    /**
     * Bidirectional
     * This field is for patient purpose.
     * By using this field we can get list of appointments booked by patient.
     * "One" patient can have "Many" appointments scheduled.
     * MappedBy patient filed inside AppointmentDetails class.
     */
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<AppointmentDetail> appointmentDetailsForPatients = new ArrayList<>();

    /**
     * Bidirectional
     * This field is for doctor purpose.
     * By using this field we can get list of patients who booked vaccine for specific doctor.
     * "One" doctor can have "Many" appointments records.
     * MappedBy doctor filed inside AppointmentDetails class.
     */
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<AppointmentDetail> doctorAppointmentDetails = new ArrayList<>();

    /**
     * Bidirectional
     * This filed is for patient purpose.
     * By using this field we can get list vaccination taken by patient.
     * "One" patient can have "Many" vaccination taken in the past.
     * MappedBy patient filed inside VaccinationDetails class.
     */
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<VaccinationDetail> vaccinationDetailsForPatients = new ArrayList<>();

    /**
     * Bidirectional
     * This filed is for doctor purpose.
     * By using this field we can get list of patients who took vaccine from specific doctor.
     * "One" doctor can have "Many" vaccination records.
     * MappedBy doctor filed inside VaccinationDetails class.
     */
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<VaccinationDetail> doctorVaccinationDetails = new ArrayList<>();
}
