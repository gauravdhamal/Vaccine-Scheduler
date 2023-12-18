package com.vaccinescheduler.dtos.response;

import com.vaccinescheduler.models.Person;
import com.vaccinescheduler.models.Vaccine;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {
    private Integer inventoryId;
    private Integer vaccineCount;
    private String batchNumber;
    private String storageTemperature;
    private LocalDateTime lastUpdated;
    private String status;
    private Integer managerId;
    private String managerFirstName;
    private String managerUsername;
}
