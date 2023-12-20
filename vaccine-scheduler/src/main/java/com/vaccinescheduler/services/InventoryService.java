package com.vaccinescheduler.services;

import com.vaccinescheduler.dtos.request.InventoryRequest;
import com.vaccinescheduler.dtos.request.VaccineListRequest;
import com.vaccinescheduler.dtos.response.InventoryResponse;
import com.vaccinescheduler.dtos.response.VaccineResponse;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.models.Vaccine;

import java.util.List;

public interface InventoryService {
    InventoryResponse createInventory(InventoryRequest inventoryRequest) throws GeneralException;
    InventoryResponse getInventory(Integer inventoryId) throws GeneralException;
    InventoryResponse updateInventory(Integer inventoryId, InventoryRequest inventoryRequest) throws GeneralException;
    Boolean deleteInventory(Integer inventoryId) throws GeneralException;
    List<InventoryResponse> getAllInventories() throws GeneralException;
    List<VaccineResponse> getAllVaccinesByInventoryId(Integer inventoryId) throws GeneralException;
    String assignManagerToInventory(Integer inventoryId, Integer managerId) throws GeneralException;
    String addVaccinesToInventory(VaccineListRequest vaccineListRequest) throws GeneralException;
}
