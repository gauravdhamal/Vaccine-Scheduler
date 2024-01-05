package com.vaccinescheduler.controllers;

import com.vaccinescheduler.dtos.response.AppointmentResponse;
import com.vaccinescheduler.dtos.response.PersonResponse;
import com.vaccinescheduler.dtos.response.VaccinationResponse;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/patient")
public class PatientController {
    @Autowired
    private PatientService patientService;
    @GetMapping("/appointments/{patientId}")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByPatientId(@PathVariable(value = "patientId") Integer patientId) throws GeneralException {
        List<AppointmentResponse> appointments = patientService.getAppointments(patientId);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<List<PersonResponse>> getAllPatients() throws GeneralException {
        List<PersonResponse> allPatients = patientService.getAllPatients();
        return new ResponseEntity<>(allPatients, HttpStatus.OK);
    }
    @GetMapping("/vaccinations/{patientId}")
    public ResponseEntity<List<VaccinationResponse>> getVaccinationsByPatientId(@PathVariable(value = "patientId") Integer patientId) throws GeneralException {
        List<VaccinationResponse> vaccinationDetails = patientService.getVaccinationDetails(patientId);
        return new ResponseEntity<>(vaccinationDetails, HttpStatus.OK);
    }
}
