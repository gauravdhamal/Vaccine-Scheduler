package com.vaccinescheduler.controllers;

import com.vaccinescheduler.dtos.response.AppointmentResponse;
import com.vaccinescheduler.dtos.response.PersonResponse;
import com.vaccinescheduler.dtos.response.VaccinationResponse;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/patient")
public class PatientController {
    @Autowired
    private PatientService patientService;
    @GetMapping("/appointments/{patientId}")
    public ResponseEntity<List<AppointmentResponse>> getAppointments(@PathVariable(value = "patientId") Integer patientId) throws GeneralException {
        List<AppointmentResponse> appointments = patientService.getAppointments(patientId);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<List<PersonResponse>> getAllPatients() throws GeneralException {
        List<PersonResponse> allPatients = patientService.getAllPatients();
        return new ResponseEntity<>(allPatients, HttpStatus.OK);
    }
    @GetMapping("/vaccinations/{patientId}")
    public ResponseEntity<List<VaccinationResponse>> getVaccinationDetails(@PathVariable(value = "patientId") Integer patientId) throws GeneralException {
        List<VaccinationResponse> vaccinationDetails = patientService.getVaccinationDetails(patientId);
        return new ResponseEntity<>(vaccinationDetails, HttpStatus.OK);
    }
}
