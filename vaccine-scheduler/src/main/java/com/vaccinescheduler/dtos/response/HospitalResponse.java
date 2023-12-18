package com.vaccinescheduler.dtos.response;

import com.vaccinescheduler.models.Address;
import com.vaccinescheduler.models.Inventory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.OneToOne;

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
}
