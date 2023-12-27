package com.vaccinescheduler.dtos.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SlotRequest {
    @FutureOrPresent(message = "slotDate must be present or future.")
    @NotNull(message = "slotDate must be not null.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate slotDate;

    @NotNull(message = "startTime must be not null.")
    @ApiModelProperty(example = "13:00:00")
    private LocalTime startTime;

    @NotNull(message = "endTime must be not null.")
    @ApiModelProperty(example = "18:00:00")
    private LocalTime endTime;

    @NotNull(message = "slotCount must be not null.")
    @Min(value = 0, message = "slotCount must be greater than equal to 0.")
    private Integer slotCount;

    @NotNull(message = "vaccineId must not be null.")
    @Min(value = 1, message = "vaccineId value must be greater than 0.")
    private Integer vaccineId;
}
