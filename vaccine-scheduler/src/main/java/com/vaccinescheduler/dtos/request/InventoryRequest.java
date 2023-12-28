package com.vaccinescheduler.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequest {
    @NotNull
    @Min(value = 0, message = "value must be greater than equal to 0.")
    private Integer vaccineCount;

    @NotNull(message = "batchNumber must not be null.")
    @NotEmpty(message = "batchNumber must not be empty.")
    @NotBlank(message = "batchNumber must not be blank.")
    private String batchNumber;

    @NotNull(message = "storageTemperature must not be null.")
    @NotEmpty(message = "storageTemperature must not be empty.")
    @NotBlank(message = "storageTemperature must not be blank.")
    private String storageTemperature;
}
