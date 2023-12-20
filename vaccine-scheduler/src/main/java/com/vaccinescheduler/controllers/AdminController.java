package com.vaccinescheduler.controllers;

import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @PutMapping("/updateInventoryManager/{adminId}/{inventoryId}")
    public ResponseEntity<String> updateInventoryManager(@PathVariable(value = "adminId") Integer adminId,@PathVariable(value = "inventoryId") Integer inventoryId) throws GeneralException {
        String updated = adminService.updateInventoryManager(adminId, inventoryId);
        return new ResponseEntity<>(updated, HttpStatus.ACCEPTED);
    }
}
