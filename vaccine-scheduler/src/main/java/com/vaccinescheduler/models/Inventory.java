package com.vaccinescheduler.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer inventoryId;

    private Integer availableVaccineCount;
    private String batchNumber;
    private String storageTemperature;
    private LocalDateTime lastUpdated;
    /**
     * in-stock and out-of-stock
     */
    private String status;

    /**
     * Bidirectional
     * This is the owner field of relationship for person class "inventory" variable.
     */
    @OneToOne
    @JoinColumn(name = "managerId")
    private Person manager;

    /**
     * Uni-directional
     * "One" inventory contain "Many" vaccines.
     */
    @OneToMany
    private List<Vaccine> vaccines = new ArrayList<>();
}
