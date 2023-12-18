package com.vaccinescheduler.dtos.request;

import com.vaccinescheduler.models.Person;
import com.vaccinescheduler.models.Vaccine;
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
public class SlotRequest {
    private Integer slotId;
    private LocalDate slotDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer availableCount;
    private Integer vaccineId;
}
