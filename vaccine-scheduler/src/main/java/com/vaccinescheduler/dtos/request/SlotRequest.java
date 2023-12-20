package com.vaccinescheduler.dtos.request;

import io.swagger.annotations.ApiModelProperty;
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
    private LocalDate slotDate;
    @ApiModelProperty(example = "13:00:00")
    private LocalTime startTime;
    @ApiModelProperty(example = "18:00:00")
    private LocalTime endTime;
    private Integer availableCount;
    private Integer vaccineId;
}
