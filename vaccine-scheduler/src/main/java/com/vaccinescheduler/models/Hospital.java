package com.vaccinescheduler.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Hospital {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer hospitalId;

    private String hospitalName;
    private String hospitalPinCode;
    private String operatingHours;
    /**
     * general, speciality, clinic
     */
    private String hospitalType;
    private Address address;

    /**
     * Uni-directional
     * This is the owner side of relationship between hospital and inventory.
     */
    @OneToOne
    private Inventory inventory;

    /**
     * Uni-directional
     * "One" hospital can have multiple payments data.
     */
    @OneToMany
    private List<PaymentDetail> paymentDetails = new ArrayList<>();

    /**
     * Uni-directional
     * "One" hospital can have multiple appointments data.
     */
    @OneToMany
    private List<AppointmentDetail> appointmentDetails = new ArrayList<>();

    /**
     * Bidirectional
     * "One" hospital can have "Many" doctors present.
     * Mapped by "hospital" variable present in person(doctor) class.
     */
    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL)
    private List<Person> doctors = new ArrayList<>();
}
