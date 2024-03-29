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
public class Slot {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer slotId;

    private LocalDate slotDate;
    /**
     * Start time should be 9, 13, 17
     */
    private LocalTime startTime;
    /**
     * End time should be 12, 16, 19
     */
    private LocalTime endTime;
    private Integer availableSlots;

    /**
     * Bidirectional
     * We can get the doctor information from slot.
     * This is the owning side of relationship between doctor and slot.
     */
    @ManyToOne
    @JoinColumn(name = "doctorId")
    private Person doctor;

    /**
     * Uni-directional
     * This is for searching slots according to vaccine name.
     * Doctor can give only one type of vaccine in time slot (e.g. 10-12).
     * We can return all the slots available for particular vaccine by name.
     */
    @OneToOne
    private Vaccine vaccine;
}
