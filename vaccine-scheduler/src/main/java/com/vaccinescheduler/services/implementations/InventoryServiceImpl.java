package com.vaccinescheduler.services.implementations;

import com.vaccinescheduler.dtos.request.InventoryRequest;
import com.vaccinescheduler.dtos.request.VaccineListRequest;
import com.vaccinescheduler.dtos.response.InventoryResponse;
import com.vaccinescheduler.dtos.response.VaccineResponse;
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

import java.time.LocalDateTime;
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
        inventory.setLastUpdated(LocalDateTime.now());
        if(inventory.getAvailableVaccineCount() > 0) {
            inventory.setStatus("in-stock");
        } else {
            inventory.setStatus("out-of-stock");
            inventory.setAvailableVaccineCount(0);
        }
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
            if(inventoryRequest.getAvailableVaccineCount() != null) {
                if(inventoryRequest.getAvailableVaccineCount() > 0) {
                    oldInventory.setStatus("in-stock");
                    oldInventory.setAvailableVaccineCount(inventoryRequest.getAvailableVaccineCount());
                } else {
                    oldInventory.setStatus("out-of-stock");
                    oldInventory.setAvailableVaccineCount(0);
                }
            }
            if(inventoryRequest.getStorageTemperature() != null) {
                oldInventory.setStorageTemperature(inventoryRequest.getStorageTemperature());
            }
            oldInventory.setLastUpdated(LocalDateTime.now());
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
    public List<InventoryResponse> getAllInventories() throws GeneralException {
        List<Inventory> inventories = inventoryRepo.findAll();
        if(!inventories.isEmpty()) {
            List<InventoryResponse> inventoryResponses = new ArrayList<>();
            for(Inventory inventory : inventories) {
                InventoryResponse inventoryResponse = modelMapper.map(inventory, InventoryResponse.class);
                inventoryResponses.add(inventoryResponse);
            }
            return inventoryResponses;
        } else {
            throw new GeneralException("No any inventory found in database.");
        }
    }

    @Override
    public List<VaccineResponse> getAllVaccinesByInventoryId(Integer inventoryId) throws GeneralException {
        Optional<Inventory> inventoryById = inventoryRepo.findById(inventoryId);
        if(inventoryById.isPresent()) {
            Inventory inventory = inventoryById.get();
            if(!inventory.getVaccines().isEmpty()) {
                List<Vaccine> vaccines = inventory.getVaccines();
                List<VaccineResponse> vaccineResponses = new ArrayList<>();
                for(Vaccine vaccine : vaccines) {
                    VaccineResponse vaccineResponse = modelMapper.map(vaccine, VaccineResponse.class);
                    vaccineResponse.setAgeRange(vaccine.getMinAge()+" - "+ vaccine.getMaxAge());
                    vaccineResponses.add(vaccineResponse);
                }
                return vaccineResponses;
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
            if(managerById.isPresent()) {
                Person manager = managerById.get();
                if(managerById.get().getRole().toLowerCase().endsWith("admin")) {
                    if(manager.getInventory() == null) {
                        if(inventory.getManager() == null) {
                            inventory.setManager(manager);
                            manager.setInventory(inventory);
                            inventoryRepo.save(inventory);
                            personRepo.save(manager);
                            return "Admin with ID : { "+managerId+" } & username { "+manager.getUsername()+" } assigned to inventory as a manager to inventory ID : "+inventoryId;
                        } else {
                            throw new GeneralException("Inventory with ID : { "+inventoryId+" } already registered with another manager Id : { "+inventory.getManager().getPersonId()+" }.");
                        }
                    } else {
                        throw new GeneralException("Manager with ID : { "+managerId+" } already registered with another inventory Id : { "+manager.getInventory().getInventoryId()+" }.");
                    }
                } else {
                    throw new GeneralException("Id : { "+manager.getPersonId()+" }, Username : { "+manager.getUsername()+" }, Role : { "+manager.getRole()+" }. Person must be admin to assign with inventory. Enter correct admin Id.");
                }
            } else {
                throw new GeneralException("Manager not found with ID : "+managerId);
            }
        } else {
            throw new GeneralException("Inventory not found with ID : "+inventoryId);
        }
    }

    @Override
    public String addVaccinesToInventory(VaccineListRequest vaccineListRequest) throws GeneralException {
        Integer inventoryId = vaccineListRequest.getInventoryId();
        List<Integer> vaccineIds = vaccineListRequest.getVaccineIds();
        Optional<Inventory> inventoryById = inventoryRepo.findById(inventoryId);
        if(inventoryById.isPresent()) {
            Inventory inventory = inventoryById.get();
            if(inventory.getManager() != null) {
                List<Vaccine> oldVaccines = inventory.getVaccines();
                List<Integer> idsToRemove = new ArrayList<>();
                for(Integer newId : vaccineIds) {
                    if(oldVaccines.stream().anyMatch(oldId -> oldId.getVaccineId() == newId)) {
                        idsToRemove.add(newId);
                    }
                }
                vaccineIds.removeAll(idsToRemove);
                List<Vaccine> vaccines = new ArrayList<>();
                Boolean vaccineNotFoundCheck = false;
                StringBuilder vaccineNotFoundResult = new StringBuilder();
                Boolean vaccineFoundCheck = false;
                StringBuilder vaccineFoundResult = new StringBuilder();
                for(Integer vaccineId : vaccineIds) {
                    Optional<Vaccine> vaccineById = vaccineRepo.findById(vaccineId);
                    if(vaccineById.isPresent()) {
                        vaccineFoundCheck = true;
                        vaccineFoundResult.append("'"+vaccineId+"'"+" ");
                        Vaccine vaccine = vaccineById.get();
                        vaccines.add(vaccine);
                    } else {
                        vaccineNotFoundCheck = true;
                        vaccineNotFoundResult.append("'"+vaccineId+"'"+" ");
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
                    return "No any vaccine found with IDs { "+vaccineNotFoundResult+"}. Please enter correct Ids.";
                }
            } else {
                throw new GeneralException("Manager not assigned to inventory. Assign it first.");
            }
        } else {
            throw new GeneralException("Inventory not found with ID : "+inventoryId);
        }
    }
}
