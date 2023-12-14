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

    private Integer vaccineCount;
    private String batchNumber;
    private String storageTemperature;
    private LocalDateTime lastUpdated;
    private String status;

    /**
     * Bidirectional
     * This is the owner field of relationship for person class "inventory" variable.
     */
    @OneToOne
    @JoinColumn(name = "managerId")
    private Person manager;

    /**
     * Bidirectional
     * mappedBy "inventory" field in Hospital class.
     */
    @OneToOne(mappedBy = "inventory", cascade = CascadeType.ALL)
    private Hospital hospital;

    /**
     * Uni-directional
     * "One" inventory contain "Many" vaccines.
     */
    @OneToMany
    private List<Vaccine> vaccines = new ArrayList<>();
}
