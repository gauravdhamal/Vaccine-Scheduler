package com.vaccinescheduler.services;

import com.vaccinescheduler.dtos.response.AppointmentResponse;
import com.vaccinescheduler.dtos.response.PersonResponse;
import com.vaccinescheduler.exceptions.GeneralException;

import java.util.List;

public interface PatientService {
    List<AppointmentResponse> getAppointments(Integer patientId) throws GeneralException;
    List<PersonResponse> getAllPatients() throws GeneralException;
}
