package com.vaccinescheduler.controllers;

import com.vaccinescheduler.dtos.request.InventoryRequest;
import com.vaccinescheduler.dtos.request.VaccineListRequest;
import com.vaccinescheduler.dtos.response.InventoryResponse;
import com.vaccinescheduler.dtos.response.VaccineResponse;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.models.Vaccine;
import com.vaccinescheduler.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import javax.validation.Valid;
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;
    @PostMapping("/create")
    public ResponseEntity<InventoryResponse> createInventory(@Valid @RequestBody InventoryRequest inventoryRequest) throws GeneralException {
        InventoryResponse inventoryResponse = inventoryService.createInventory(inventoryRequest);
        return new ResponseEntity<>(inventoryResponse, HttpStatus.CREATED);
    }
    @GetMapping("/get/{inventoryId}")
    public ResponseEntity<InventoryResponse> getInventory(@PathVariable(value = "inventoryId") Integer inventoryId) throws GeneralException {
        InventoryResponse inventoryResponse = inventoryService.getInventory(inventoryId);
        return new ResponseEntity<>(inventoryResponse, HttpStatus.OK);
    }
    @PutMapping("/update/{inventoryId}")
    public ResponseEntity<InventoryResponse> updateInventory(@PathVariable(value = "inventoryId") Integer inventoryId,@Valid @RequestBody InventoryRequest inventoryRequest) throws GeneralException {
        InventoryResponse inventoryResponse = inventoryService.updateInventory(inventoryId, inventoryRequest);
        return new ResponseEntity<>(inventoryResponse, HttpStatus.OK);
    }
    @DeleteMapping("/delete/{inventoryId}")
    public ResponseEntity<Boolean> deleteInventory(@PathVariable(value = "inventoryId") Integer inventoryId) throws GeneralException {
        Boolean deleted = inventoryService.deleteInventory(inventoryId);
        return new ResponseEntity<>(deleted, HttpStatus.NO_CONTENT);
    }
    @GetMapping("/all")
    public ResponseEntity<List<InventoryResponse>> getAllInventories() throws GeneralException {
        List<InventoryResponse> allInventories = inventoryService.getAllInventories();
        return new ResponseEntity<>(allInventories, HttpStatus.OK);
    }
    @GetMapping("/allVaccinesByInventoryId/{inventoryId}")
    public ResponseEntity<List<VaccineResponse>> getAllVaccinesByInventoryId(@PathVariable(value = "inventoryId") Integer inventoryId) throws GeneralException {
        List<VaccineResponse> vaccineResponses = inventoryService.getAllVaccinesByInventoryId(inventoryId);
        return new ResponseEntity<>(vaccineResponses, HttpStatus.OK);
    }
    @PutMapping("/assignManagerToInventory/{inventoryId}/{managerId}")
    public ResponseEntity<String> assignManagerToInventory(@PathVariable(value = "inventoryId") Integer inventoryId,@PathVariable(value = "managerId") Integer managerId) throws GeneralException {
        String result = inventoryService.assignManagerToInventory(inventoryId, managerId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @PutMapping("/addVaccinesToInventory")
    public ResponseEntity<String> addVaccinesToInventory(@Valid @RequestBody VaccineListRequest vaccineListRequest) throws GeneralException {
        String result = inventoryService.addVaccinesToInventory(vaccineListRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
