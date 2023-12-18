package com.vaccinescheduler.services.implementations;

import com.vaccinescheduler.dtos.request.InventoryRequest;
import com.vaccinescheduler.dtos.request.VaccineList;
import com.vaccinescheduler.dtos.response.InventoryResponse;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.models.Inventory;
import com.vaccinescheduler.models.Person;
import com.vaccinescheduler.models.Vaccine;
import com.vaccinescheduler.repositories.InventoryRepo;
import com.vaccinescheduler.repositories.PersonRepo;
import com.vaccinescheduler.repositories.VaccineRepo;
import com.vaccinescheduler.services.InventoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {
    @Autowired
    private InventoryRepo inventoryRepo;
    @Autowired
    private PersonRepo personRepo;
    @Autowired
    private VaccineRepo vaccineRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public InventoryResponse createInventory(InventoryRequest inventoryRequest) throws GeneralException {
        Inventory inventory = modelMapper.map(inventoryRequest, Inventory.class);
        inventory = inventoryRepo.save(inventory);
        InventoryResponse inventoryResponse = modelMapper.map(inventory, InventoryResponse.class);
        return inventoryResponse;
    }

    @Override
    public InventoryResponse getInventory(Integer inventoryId) throws GeneralException {
        Optional<Inventory> inventoryById = inventoryRepo.findById(inventoryId);
        if(inventoryById.isPresent()) {
            InventoryResponse inventoryResponse = modelMapper.map(inventoryById.get(), InventoryResponse.class);
            return inventoryResponse;
        } else {
            throw new GeneralException("Inventory not found with ID : "+inventoryId);
        }
    }

    @Override
    public InventoryResponse updateInventory(Integer inventoryId, InventoryRequest inventoryRequest) throws GeneralException {
        Optional<Inventory> inventoryById = inventoryRepo.findById(inventoryId);
        if(inventoryById.isPresent()) {
            Inventory oldInventory = inventoryById.get();
            if(inventoryRequest.getStatus() != null) {
                oldInventory.setStatus(inventoryRequest.getStatus());
            }
            if(inventoryRequest.getStorageTemperature() != null) {
                oldInventory.setStorageTemperature(inventoryRequest.getStorageTemperature());
            }
            oldInventory = inventoryRepo.save(oldInventory);
            InventoryResponse inventoryResponse = modelMapper.map(oldInventory, InventoryResponse.class);
            return inventoryResponse;
        } else {
            throw new GeneralException("Inventory not found with ID : "+inventoryId);
        }
    }

    @Override
    public Boolean deleteInventory(Integer inventoryId) throws GeneralException {
        Optional<Inventory> inventoryById = inventoryRepo.findById(inventoryId);
        if(inventoryById.isPresent()) {
            Inventory oldInventory = inventoryById.get();
            inventoryRepo.delete(oldInventory);
            return true;
        } else {
            throw new GeneralException("Inventory not found with ID : "+inventoryId);
        }
    }

    @Override
    public List<Vaccine> getAllVaccinesByInventoryId(Integer inventoryId) throws GeneralException {
        Optional<Inventory> inventoryById = inventoryRepo.findById(inventoryId);
        if(inventoryById.isPresent()) {
            Inventory inventory = inventoryById.get();
            if(!inventory.getVaccines().isEmpty()) {
                return inventory.getVaccines();
            } else {
                throw new GeneralException("No any vaccines found in inventory ID : "+inventoryId);
            }
        } else {
            throw new GeneralException("Inventory not found with ID : "+inventoryId);
        }
    }

    @Override
    public String assignManagerToInventory(Integer inventoryId, Integer managerId) throws GeneralException {
        Optional<Inventory> inventoryById = inventoryRepo.findById(inventoryId);
        if(inventoryById.isPresent()) {
            Inventory inventory = inventoryById.get();
            Optional<Person> managerById = personRepo.findById(managerId);
            if(managerById.isPresent() && managerById.get().getRole().toLowerCase().endsWith("admin")) {
                Person manager = managerById.get();
                if(manager.getInventory() == null) {
                    if(inventory.getManager() == null) {
                        inventory.setManager(manager);
                        manager.setInventory(inventory);
                        inventoryRepo.save(inventory);
                        personRepo.save(manager);
                        return "Admin with ID : { "+managerId+" } & username { "+manager.getUsername()+" } assigned to inventory as an manager to inventory ID : "+inventoryId;
                    } else {
                        throw new GeneralException("Inventory with ID : { "+inventoryId+" } already registered with another manager.");
                    }
                } else {
                    throw new GeneralException("Manager with ID : { "+managerId+" } already registered with another inventory.");
                }
            } else {
                throw new GeneralException("Manager not found with ID : "+managerId);
            }
        } else {
            throw new GeneralException("Inventory not found with ID : "+inventoryId);
        }
    }

    @Override
    public String addVaccinesToInventory(VaccineList vaccineList) throws GeneralException {
        Integer inventoryId = vaccineList.getInventoryId();
        Optional<Inventory> inventoryById = inventoryRepo.findById(inventoryId);
        if(inventoryById.isPresent()) {
            Inventory inventory = inventoryById.get();
            List<Vaccine> vaccines = new ArrayList<>();
            Boolean vaccineNotFoundCheck = false;
            StringBuilder vaccineNotFoundResult = new StringBuilder();
            Boolean vaccineFoundCheck = false;
            StringBuilder vaccineFoundResult = new StringBuilder();
            for(Integer vaccineId : vaccineList.getVaccineIds()) {
                Optional<Vaccine> vaccineById = vaccineRepo.findById(vaccineId);
                if(vaccineById.isPresent()) {
                    vaccineFoundCheck = true;
                    vaccineFoundResult.append(vaccineId+" ");
                    Vaccine vaccine = vaccineById.get();
                } else {
                    vaccineNotFoundCheck = true;
                    vaccineNotFoundResult.append(vaccineId+" ");
                }
            }
            if(vaccineFoundCheck && vaccineNotFoundCheck) {
                inventory.getVaccines().addAll(vaccines);
                inventoryRepo.save(inventory);
                return vaccineFoundResult+": Vaccines added to inventory. Some vaccines not found : "+vaccineNotFoundResult;
            } else if(vaccineFoundCheck) {
                inventory.getVaccines().addAll(vaccines);
                inventoryRepo.save(inventory);
                return "Vaccines added to inventory : "+vaccineFoundResult;
            } else {
                return "No any vaccine found with IDs { "+vaccineNotFoundResult+" }. Please enter correct Ids.";
            }
        } else {
            throw new GeneralException("Inventory not found with ID : "+inventoryId);
        }
    }
}
