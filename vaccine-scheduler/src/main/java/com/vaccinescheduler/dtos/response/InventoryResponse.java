package com.vaccinescheduler.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {
    private Integer inventoryId;
    private Integer availableVaccineCount;
    private String batchNumber;
    private String storageTemperature;
    private LocalDateTime lastUpdated;
    private String status;
    private Integer managerId;
    private String managerFirstName;
    private String managerUsername;
}
