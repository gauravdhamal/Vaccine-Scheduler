package com.vaccinescheduler.controllers;

import com.vaccinescheduler.dtos.request.DoctorListRequest;
import com.vaccinescheduler.dtos.request.HospitalRequest;
import com.vaccinescheduler.dtos.response.AppointmentResponse;
import com.vaccinescheduler.dtos.response.HospitalResponse;
import com.vaccinescheduler.dtos.response.PaymentDetailResponse;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.services.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hospital")
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;
    @PostMapping("/create")
    public ResponseEntity<HospitalResponse> createHospital(@RequestBody HospitalRequest hospitalRequest) throws GeneralException {
        HospitalResponse hospitalResponse = hospitalService.createHospital(hospitalRequest);
        return new ResponseEntity<>(hospitalResponse, HttpStatus.CREATED);
    }
    @GetMapping("/get/{hospitalId}")
    public ResponseEntity<HospitalResponse> getHospital(@PathVariable(value = "hospitalId") Integer hospitalId) throws GeneralException {
        HospitalResponse hospitalResponse = hospitalService.getHospital(hospitalId);
        return new ResponseEntity<>(hospitalResponse, HttpStatus.OK);
    }
    @PutMapping("/update/{hospitalId}")
    public ResponseEntity<HospitalResponse> updateHospital(@PathVariable(value = "hospitalId") Integer hospitalId,@RequestBody HospitalRequest hospitalRequest) throws GeneralException {
        HospitalResponse hospitalResponse = hospitalService.updateHospital(hospitalId, hospitalRequest);
        return new ResponseEntity<>(hospitalResponse, HttpStatus.OK);
    }
    @DeleteMapping("/delete/{hospitalId}")
    public ResponseEntity<Boolean> deleteHospital(@PathVariable(value = "hospitalId") Integer hospitalId) throws GeneralException {
        Boolean deleted = hospitalService.deleteHospital(hospitalId);
        return new ResponseEntity<>(deleted, HttpStatus.NO_CONTENT);
    }
    @PutMapping("/addInventoryToHospital/{hospitalId}/{inventoryId}")
    public ResponseEntity<String> addInventoryToHospital(@PathVariable(value = "hospitalId") Integer hospitalId,@PathVariable(value = "inventoryId") Integer inventoryId) throws GeneralException {
        String added = hospitalService.addInventoryToHospital(hospitalId, inventoryId);
        return new ResponseEntity<>(added, HttpStatus.OK);
    }
    @GetMapping("/allPayments/{hospitalId}")
    public ResponseEntity<List<PaymentDetailResponse>> getAllPayments(@PathVariable(value = "hospitalId") Integer hospitalId) throws GeneralException {
        List<PaymentDetailResponse> allPayments = hospitalService.getAllPayments(hospitalId);
        return new ResponseEntity<>(allPayments, HttpStatus.OK);
    }
    @GetMapping("/getAllAppointments/{hospitalId}")
    public ResponseEntity<List<AppointmentResponse>> getAllAppointments(@PathVariable(value = "hospitalId") Integer hospitalId) throws GeneralException {
        List<AppointmentResponse> allAppointments = hospitalService.getAllAppointments(hospitalId);
        return new ResponseEntity<>(allAppointments, HttpStatus.OK);
    }
    @PutMapping("/addDoctorsToHospital")
    public ResponseEntity<String> addDoctorsToHospital(@RequestBody DoctorListRequest doctorListRequest) throws GeneralException {
        String added = hospitalService.addDoctorsToHospital(doctorListRequest);
        return new ResponseEntity<>(added, HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<List<HospitalResponse>> getAllHospitals() throws GeneralException {
        List<HospitalResponse> hospitalResponses = hospitalService.getAllHospitals();
        return new ResponseEntity<>(hospitalResponses, HttpStatus.OK);
    }
}
