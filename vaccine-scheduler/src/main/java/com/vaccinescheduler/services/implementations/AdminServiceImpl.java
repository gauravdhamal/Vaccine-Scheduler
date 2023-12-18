package com.vaccinescheduler.services.implementations;

import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.models.Inventory;
import com.vaccinescheduler.models.Person;
import com.vaccinescheduler.repositories.InventoryRepo;
import com.vaccinescheduler.repositories.PersonRepo;
import com.vaccinescheduler.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private PersonRepo personRepo;
    @Autowired
    private InventoryRepo inventoryRepo;
    @Override
    public String updateInventoryManager(Integer adminId, Integer inventoryId) throws GeneralException {
        Optional<Person> personById = personRepo.findById(adminId);
        if(personById.isPresent()) {
            Person admin = personById.get();
            Optional<Inventory> inventoryById = inventoryRepo.findById(inventoryId);
            if(inventoryById.isPresent()) {
                Inventory inventory = inventoryById.get();
                if(admin.getRole().toLowerCase().endsWith("admin")) {
                    if(inventory.getManager() != null) {
                        if(inventory.getManager().getPersonId() == admin.getPersonId()) {
                            inventory.setManager(admin);
                            admin.setInventory(inventory);
                            admin = personRepo.save(admin);
                            inventoryRepo.save(inventory);
                            return "{ "+admin.getUsername()+" } added to inventory { "+inventoryId+" } as a manager.";
                        } else {
                            throw new GeneralException("Admin { "+adminId+" } does not belongs to inventory { "+inventoryId+" }.");
                        }
                    } else {
                        throw new GeneralException("Inventory don't have any manager assigned to it.");
                    }
                } else {
                    throw new GeneralException("ID : { "+adminId+" }, username : { "+admin.getUsername()+" } is not a admin. Enter correct admin ID.");
                }
            } else {
                throw new GeneralException("Inventory not found with ID : "+inventoryId);
            }
        } else {
            throw new GeneralException("Person not found with ID : "+adminId+". Enter the correct admin ID.");
        }
    }
}
