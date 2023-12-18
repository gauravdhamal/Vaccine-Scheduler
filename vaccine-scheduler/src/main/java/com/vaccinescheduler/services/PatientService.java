package com.vaccinescheduler.services;

import com.vaccinescheduler.dtos.response.AppointmentResponse;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.models.AppointmentDetail;

import java.util.List;

public interface PatientService {
    List<AppointmentResponse> getAppointments(Integer patientId) throws GeneralException;
}
