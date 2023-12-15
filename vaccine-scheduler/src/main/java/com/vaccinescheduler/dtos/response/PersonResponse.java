package com.vaccinescheduler.dtos.response;

import com.vaccinescheduler.models.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonResponse {
    private Integer personId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String role;
    private String specialization;
    private Integer age;
    @Column(unique = true)
    private String aadhaarNumber;
    private Address address;
    @Column(unique = true)
    private String username;
}
