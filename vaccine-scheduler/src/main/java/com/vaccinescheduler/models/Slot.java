package com.vaccinescheduler.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Slot {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer slotId;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer availableCount;

    /**
     * This is for searching slots according to vaccine name.
     * Doctor can give only one type of vaccine in time slot (e.g. 10-12).
     * We can return all the slots available for particular vaccine by name.
     */
    @OneToOne
    private Vaccine vaccine;

    /**
     * This is the owner field of relationship between user(doctor) and slot.
     * "Many" slots belong to "One" doctor.
     */
    @ManyToOne
    @JoinColumn(name = "userId")
    private User doctor;
}
