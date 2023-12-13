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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userId;

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private Address address;
    private String role;
    private String specialization;
    private Integer age;
    @Column(unique = true)
    private String username;
    private String password;

    /**
     * This field is for admin purpose.
     * "One" admin can be assigned to "One" inventory at a time.
     * Mapped by "admin" variable present in Inventory class.
     * Because inventory must know about the admin.
     */
    @OneToOne(mappedBy = "admin", cascade = CascadeType.ALL)
    private Inventory inventory;

    /**
     * This field is for doctor purpose.
     * "Many" doctors can be assigned to "One" hospital at a time.
     * This is the owner field for "Hospital" class list of doctors.
     */
    @ManyToOne
    @JoinColumn(name = "hospitalId")
    private Hospital hospital;

    /**
     * This field is for doctor purpose.
     * "One" doctor can have "Many" slots available per day.
     */
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<Slot> slots = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<VaccinationDetails> vaccinationDetailsList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AppointmentDetails> appointmentDetailsList = new ArrayList<>();
}
