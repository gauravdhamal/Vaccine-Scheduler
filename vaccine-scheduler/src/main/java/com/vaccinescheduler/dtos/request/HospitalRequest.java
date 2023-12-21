package com.vaccinescheduler.dtos.request;

import com.vaccinescheduler.models.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HospitalRequest {
    private String hospitalName;
    private String hospitalPinCode;
    private String operatingHours;
    private String hospitalType;
    private Address address;
}
