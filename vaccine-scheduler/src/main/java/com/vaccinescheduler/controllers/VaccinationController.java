package com.vaccinescheduler.controllers;

import com.vaccinescheduler.dtos.response.VaccinationResponse;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.services.VaccinationDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/vaccination")
public class VaccinationController {
    @Autowired
    private VaccinationDetailService vaccinationDetailService;
    @PutMapping("/updateVaccinationDetails")
    public ResponseEntity<List<VaccinationResponse>> updateVaccinationDetails() throws GeneralException {
        List<VaccinationResponse> vaccinationResponses = vaccinationDetailService.updateVaccinationRecord();
        return new ResponseEntity<>(vaccinationResponses, HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<List<VaccinationResponse>> getAllVaccinationDetails() throws GeneralException {
        List<VaccinationResponse> allVaccinationRecords = vaccinationDetailService.getAllVaccinationRecords();
        return new ResponseEntity<>(allVaccinationRecords, HttpStatus.OK);
    }
    @GetMapping("/details/{date}/{slot}")
    public ResponseEntity<List<VaccinationResponse>> getVaccinationDetailsByDateAndSlot(@PathVariable(value = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @PathVariable(value = "slot") String slot) throws GeneralException {
        List<VaccinationResponse> patientsByDateAndSlot = vaccinationDetailService.getVaccinationDetailsByDateAndSlot(date, slot);
        return new ResponseEntity<>(patientsByDateAndSlot, HttpStatus.OK);
    }
}
