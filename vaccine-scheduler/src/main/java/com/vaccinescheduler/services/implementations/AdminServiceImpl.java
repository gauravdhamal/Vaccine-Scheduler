package com.vaccinescheduler.services.implementations;

import com.vaccinescheduler.dtos.response.PersonResponse;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.models.Inventory;
import com.vaccinescheduler.models.Person;
import com.vaccinescheduler.repositories.InventoryRepo;
import com.vaccinescheduler.repositories.PersonRepo;
import com.vaccinescheduler.services.AdminService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private PersonRepo personRepo;
    @Autowired
    private InventoryRepo inventoryRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public String updateInventoryManager(Integer adminId, Integer inventoryId) throws GeneralException {
        Optional<Person> managerById = personRepo.findById(adminId);
        if(managerById.isPresent()) {
            Person manager = managerById.get();
            if(manager.getRole().toLowerCase().endsWith("admin")) {
                if(manager.getInventory() == null) {
                    Optional<Inventory> inventoryById = inventoryRepo.findById(inventoryId);
                    if(inventoryById.isPresent()) {
                        Inventory inventory = inventoryById.get();
                        if(inventory.getManager() != null) {
                            Person oldManager = inventory.getManager();
                            inventory.setManager(manager);
                            manager.setInventory(inventory);
                            personRepo.save(manager);
                            inventoryRepo.save(inventory);
                            return "Manager with ID : { "+manager.getPersonId()+" } registered with the inventory : { "+inventoryId+" }. Old manager Id : { "+oldManager.getPersonId()+" } has been removed.";
                        } else {
                            return "Something wrong while { updateInventoryManager }.";
                        }
                    } else {
                        throw new GeneralException("Inventory not found with ID : "+inventoryId);
                    }
                } else {
                    throw new GeneralException("Manager is already assigned to another inventory ID : { "+manager.getInventory().getInventoryId()+" }. Can't be registered with this one.");
                }
            } else {
                throw new GeneralException("ID : { "+manager.getPersonId()+" }, Username : { "+manager.getUsername()+" }, Role : { "+manager.getRole()+" } is not a admin. Enter correct admin ID.");
            }
        } else {
            throw new GeneralException("Admin not found with ID : "+adminId+". Enter the correct admin ID.");
        }
    }

    @Override
    public List<PersonResponse> getAllAdmins() throws GeneralException {
        Optional<List<Person>> personByRole = personRepo.findByRole("ROLE_ADMIN");
        if(personByRole.isPresent() && !personByRole.get().isEmpty()) {
            List<Person> admins = personByRole.get();
            List<PersonResponse> personResponses = new ArrayList<>();
            for(Person admin : admins) {
                PersonResponse personResponse = modelMapper.map(admin, PersonResponse.class);
                personResponses.add(personResponse);
            }
            return personResponses;
        } else {
            throw new GeneralException("No any admins found in database.");
        }
    }
}
