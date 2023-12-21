package com.vaccinescheduler.controllers;

import com.vaccinescheduler.dtos.request.SlotRequest;
import com.vaccinescheduler.dtos.response.SlotResponse;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.models.Slot;
import com.vaccinescheduler.services.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/slot")
public class SlotController {
    @Autowired
    private SlotService slotService;
    @PostMapping("/create")
    public ResponseEntity<SlotResponse> createSlot(@RequestBody SlotRequest slotRequest) throws GeneralException {
        SlotResponse slot = slotService.createSlot(slotRequest);
        return new ResponseEntity<>(slot, HttpStatus.CREATED);
    }
    @GetMapping("/get/{slotId}")
    public ResponseEntity<SlotResponse> getSlot(@PathVariable(value = "slotId") Integer slotId) throws GeneralException {
        SlotResponse slot = slotService.getSlot(slotId);
        return new ResponseEntity<>(slot, HttpStatus.OK);
    }
    @PutMapping("/update/{slotId}")
    public ResponseEntity<SlotResponse> updateSlot(@PathVariable(value = "slotId") Integer slotId,@RequestBody SlotRequest slotRequest) throws GeneralException {
        SlotResponse slotResponse = slotService.updateSlot(slotId, slotRequest);
        return new ResponseEntity<>(slotResponse, HttpStatus.OK);
    }
    @DeleteMapping("/delete/{slotId}")
    public ResponseEntity<Boolean> deleteSlot(@PathVariable(value = "slotId") Integer slotId) throws GeneralException {
        Boolean deleted = slotService.deleteSlot(slotId);
        return new ResponseEntity<>(deleted, HttpStatus.NO_CONTENT);
    }
    @GetMapping("/all")
    public ResponseEntity<List<SlotResponse>> getAllSlots() throws GeneralException {
        List<SlotResponse> slotResponses = slotService.getAllSlots();
        return new ResponseEntity<>(slotResponses, HttpStatus.OK);
    }
    @GetMapping("/getSlotsByVaccineName/{vaccineName}")
    public ResponseEntity<List<SlotResponse>> getAllSlotsByVaccineName(@PathVariable(value = "vaccineName") String vaccineName) throws GeneralException {
        List<SlotResponse> slotResponses = slotService.getAllSlotsByVaccineName(vaccineName);
        return new ResponseEntity<>(slotResponses, HttpStatus.OK);
    }
}
