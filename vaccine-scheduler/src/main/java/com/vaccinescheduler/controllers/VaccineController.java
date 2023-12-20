package com.vaccinescheduler.controllers;

import com.vaccinescheduler.dtos.request.VaccineRequest;
import com.vaccinescheduler.dtos.response.VaccineResponse;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.services.VaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vaccine")
public class VaccineController {
    @Autowired
    private VaccineService vaccineService;
    @PostMapping("/create")
    public ResponseEntity<VaccineResponse> createVaccine(@RequestBody VaccineRequest vaccineRequest) throws GeneralException {
        VaccineResponse vaccineResponse = vaccineService.createVaccine(vaccineRequest);
        return new ResponseEntity<>(vaccineResponse, HttpStatus.CREATED);
    }
    @GetMapping("/get/{vaccineId}")
    public ResponseEntity<VaccineResponse> getVaccine(@PathVariable(value = "vaccineId") Integer vaccineId) throws GeneralException {
        VaccineResponse vaccineResponse = vaccineService.getVaccine(vaccineId);
        return new ResponseEntity<>(vaccineResponse, HttpStatus.OK);
    }
    @PutMapping("/update/{vaccineId}")
    public ResponseEntity<VaccineResponse> updateVaccine(@PathVariable(value = "vaccineId") Integer vaccineId,@RequestBody VaccineRequest vaccineRequest) throws GeneralException {
        VaccineResponse vaccineResponse = vaccineService.updateVaccine(vaccineId, vaccineRequest);
        return new ResponseEntity<>(vaccineResponse, HttpStatus.OK);
    }
    @DeleteMapping("/delete/{vaccineId}")
    public ResponseEntity<Boolean> deleteVaccine(@PathVariable(value = "vaccineId") Integer vaccineId) throws GeneralException {
        Boolean deleted = vaccineService.deleteVaccine(vaccineId);
        return new ResponseEntity<>(deleted, HttpStatus.NO_CONTENT);
    }
    @GetMapping("/all")
    public ResponseEntity<List<VaccineResponse>> getAllVaccines() throws GeneralException {
        List<VaccineResponse> vaccinesResponses = vaccineService.getAllVaccines();
        return new ResponseEntity<>(vaccinesResponses, HttpStatus.OK);
    }
    @GetMapping("/adult")
    public ResponseEntity<List<VaccineResponse>> getVaccinesForAdult() throws GeneralException {
        List<VaccineResponse> vaccinesForAdult = vaccineService.getVaccinesForAdult();
        return new ResponseEntity<>(vaccinesForAdult, HttpStatus.OK);
    }
    @GetMapping("/child")
    public ResponseEntity<List<VaccineResponse>> getVaccinesForChild() throws GeneralException {
        List<VaccineResponse> vaccinesForChild = vaccineService.getVaccinesForChild();
        return new ResponseEntity<>(vaccinesForChild, HttpStatus.OK);
    }
    @GetMapping("/getByName/{vaccineName}")
    public ResponseEntity<List<VaccineResponse>> findVaccineByName(@PathVariable(value = "vaccineName") String vaccineName) throws GeneralException {
        List<VaccineResponse> vaccineByName = vaccineService.findVaccineByName(vaccineName);
        return new ResponseEntity<>(vaccineByName, HttpStatus.OK);
    }
}
