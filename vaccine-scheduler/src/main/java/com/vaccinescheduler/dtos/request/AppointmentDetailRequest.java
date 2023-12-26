package com.vaccinescheduler.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDetailRequest {
    private String firstName;
    /**
     * Self, Other
     */
    private String bookingFor;
    private String gender;
    private Integer age;
    private String phone;
    private String email;
}
