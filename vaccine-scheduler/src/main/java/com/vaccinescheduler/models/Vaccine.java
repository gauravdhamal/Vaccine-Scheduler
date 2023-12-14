package com.vaccinescheduler.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vaccine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer vaccineId;

    private String vaccineName;
    private String vaccineManufacturer;
    private LocalDate manufacturedDate;
    private LocalDate expirationDate;
    private Double originalPrice;
    private Integer minAge;
    private Integer maxAge;
    private String discount;
    private Double discountedPrice;
}
