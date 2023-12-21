package com.vaccinescheduler.controllers;

import com.vaccinescheduler.dtos.request.AddSlots;
import com.vaccinescheduler.dtos.response.HospitalResponse;
import com.vaccinescheduler.dtos.response.PersonResponse;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctor")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;
    @GetMapping("/getHospitalByDoctorId/{doctorId}")
    public ResponseEntity<HospitalResponse> getAssignedHospitalByDoctorId(@PathVariable(value = "doctorId") Integer doctorId) throws GeneralException {
        HospitalResponse assignedHospitalByDoctorId = doctorService.getAssignedHospitalByDoctorId(doctorId);
        return new ResponseEntity<>(assignedHospitalByDoctorId, HttpStatus.OK);
    }
    @PutMapping("/addSlots")
    public ResponseEntity<String> addSlotsByDoctorId(@RequestBody AddSlots addSlots) throws GeneralException {
        String added = doctorService.addSlotsByDoctorId(addSlots);
        return new ResponseEntity<>(added, HttpStatus.OK);
    }
    @GetMapping("/getVaccinatedPatientsByDoctorId/{doctorId}")
    public ResponseEntity<List<PersonResponse>> getVaccinatedPatientsByDoctorId(@PathVariable(value = "doctorId") Integer doctorId) throws GeneralException {
        List<PersonResponse> vaccinatedPatientsByDoctorId = doctorService.getVaccinatedPatientsByDoctorId(doctorId);
        return new ResponseEntity<>(vaccinatedPatientsByDoctorId, HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<List<PersonResponse>> getAllDoctors() throws GeneralException {
        List<PersonResponse> allDoctors = doctorService.getAllDoctors();
        return new ResponseEntity<>(allDoctors, HttpStatus.OK);
    }
}
