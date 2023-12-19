package com.vaccinescheduler.dtos.response;

import com.vaccinescheduler.models.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HospitalResponse {
    private Integer hospitalId;
    private String hospitalName;
    private String hospitalPinCode;
    private String operatingHours;
    private String hospitalType;
    private Address address;
    private Integer availableSlots;
    private Integer inventoryId;
    private String inventoryVaccineCount;
    private String inventoryStatus;
}
