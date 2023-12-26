package com.vaccinescheduler.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DoctorListRequest {
    @NotNull(message = "hospitalId must not be null.")
    @Min(value = 1, message = "hospitalId value must be greater than 0.")
    private Integer hospitalId;

    @NotEmpty(message = "doctorId's must not be empty.")
    private List<@NotNull @Positive(message = "doctorId's must be a positive integer.") Integer> doctorIds;
}
