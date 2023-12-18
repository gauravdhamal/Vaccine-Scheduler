package com.vaccinescheduler.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SlotResponse {
    private Integer slotId;
    private LocalDate slotDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer availableCount;
    private Integer doctorId;
    private String doctorFirstName;
    private String doctorUsername;
    private Integer vaccineId;
    private String vaccineName;
}
