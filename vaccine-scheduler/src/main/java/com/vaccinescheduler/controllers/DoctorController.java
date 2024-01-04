package com.vaccinescheduler.controllers;

import com.vaccinescheduler.dtos.request.AddSlotsRequest;
import com.vaccinescheduler.dtos.response.AppointmentResponse;
import com.vaccinescheduler.dtos.response.HospitalResponse;
import com.vaccinescheduler.dtos.response.PersonResponse;
import com.vaccinescheduler.dtos.response.SlotResponse;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.models.Person;
import com.vaccinescheduler.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.validation.Valid;
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
    public ResponseEntity<String> addSlotsByDoctorId(@Valid @RequestBody AddSlotsRequest addSlotsRequest) throws GeneralException {
        String added = doctorService.addSlotsByDoctorId(addSlotsRequest);
        return new ResponseEntity<>(added, HttpStatus.OK);
    }
    @GetMapping("/getVaccinatedPatientsByDoctorId/{doctorId}")
    public ResponseEntity<List<PersonResponse>> getVaccinatedPatientsByDoctorId(@PathVariable(value = "doctorId") Integer doctorId) throws GeneralException {
        List<PersonResponse> vaccinatedPatientsByDoctorId = doctorService.getVaccinatedPatientsByDoctorId(doctorId);
        return new ResponseEntity<>(vaccinatedPatientsByDoctorId, HttpStatus.OK);
    }
    @GetMapping("/getPatientsFromAppointmentsByDoctorId/{doctorId}")
    public ResponseEntity<List<PersonResponse>> getPatientsFromAppointmentsByDoctorId(@PathVariable(value = "doctorId") Integer doctorId) throws GeneralException {
        List<PersonResponse> patientsFromAppointmentsByDoctorId = doctorService.getPatientsFromAppointmentsByDoctorId(doctorId);
        return new ResponseEntity<>(patientsFromAppointmentsByDoctorId, HttpStatus.OK);
    }
    @GetMapping("/getAppointmentDetailsByDoctorId/{doctorId}")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByDoctorId(@PathVariable(value = "doctorId") Integer doctorId) throws GeneralException {
        List<AppointmentResponse> appointmentDetailsByDoctorId = doctorService.getAppointmentDetailsByDoctorId(doctorId);
        return new ResponseEntity<>(appointmentDetailsByDoctorId, HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<List<PersonResponse>> getAllDoctors() throws GeneralException {
        List<PersonResponse> allDoctors = doctorService.getAllDoctors();
        return new ResponseEntity<>(allDoctors, HttpStatus.OK);
    }
    @GetMapping("/get/{doctorId}/allSlots")
    public ResponseEntity<List<SlotResponse>> getAllSlots(@PathVariable(value = "doctorId") Integer doctorId) throws GeneralException {
        List<SlotResponse> allSlots = doctorService.getAllSlots(doctorId);
        return new ResponseEntity<>(allSlots, HttpStatus.OK);
    }
}
