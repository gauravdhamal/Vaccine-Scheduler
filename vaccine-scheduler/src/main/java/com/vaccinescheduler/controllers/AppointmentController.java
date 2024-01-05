package com.vaccinescheduler.controllers;

import com.vaccinescheduler.dtos.request.AppointmentDetailRequest;
import com.vaccinescheduler.dtos.response.AppointmentDetailResponse;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.services.AppointmentDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/appointment")
public class AppointmentController {
    @Autowired
    private AppointmentDetailService appointmentDetailService;
    @GetMapping("/get/{appointmentDetailId}")
    public ResponseEntity<AppointmentDetailResponse> getAppointment(@PathVariable(value = "appointmentDetailId") Integer appointmentDetailId) throws GeneralException {
        AppointmentDetailResponse appointmentDetailResponse = appointmentDetailService.getAppointmentDetail(appointmentDetailId);
        return new ResponseEntity<>(appointmentDetailResponse, HttpStatus.OK);
    }
    @PutMapping("/book/{slotId}/{hospitalId}")
    public ResponseEntity<AppointmentDetailResponse> bookAppointment(@PathVariable(value = "slotId") Integer slotId,@PathVariable(value = "hospitalId") Integer hospitalId,@Valid @RequestBody AppointmentDetailRequest appointmentDetailRequest) throws GeneralException, IOException {
        AppointmentDetailResponse appointmentDetailResponse = appointmentDetailService.bookAppointment(slotId, hospitalId, appointmentDetailRequest);
        return new ResponseEntity<>(appointmentDetailResponse, HttpStatus.OK);
    }
    @PutMapping("/reschedule/{newSlotId}/{appointmentId}")
    public ResponseEntity<AppointmentDetailResponse> rescheduleAppointment(@PathVariable(value = "newSlotId") Integer newSlotId,@PathVariable(value = "appointmentId") Integer appointmentId) throws GeneralException, IOException {
        AppointmentDetailResponse appointmentDetailResponse = appointmentDetailService.rescheduleAppointment(newSlotId, appointmentId);
        return new ResponseEntity<>(appointmentDetailResponse, HttpStatus.OK);
    }
}
